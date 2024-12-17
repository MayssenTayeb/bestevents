package com.Events.bestevents.controllers;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.Events.bestevents.models.Registration;
import com.Events.bestevents.models.RegistrationDto;
import com.Events.bestevents.services.RegistrationsRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/registrations")
public class RegistrationsController {

    @Autowired
    private RegistrationsRepository repo;

    @GetMapping({"", "/"})
    public String showRegistrationsList(Model model) {
        List<Registration> registrations = repo.findAll(Sort.by(Sort.Direction.DESC, "id")); 
        model.addAttribute("registrations", registrations); 
        return "registrations/index"; 
      }

      @GetMapping("/start")
      public String showStartPage(Model model) {
        RegistrationDto registrationDto = new RegistrationDto ();
        model.addAttribute("registrationDto", registrationDto); 
        return "registrations/StartRegistration"; 
      }

      @PostMapping("/start")
	 public String startRegistration(
	     @Valid @ModelAttribute RegistrationDto registrationDto, 
	     BindingResult result
		 ) {
            if (registrationDto.getImageFile().isEmpty())  {
			 result.addError(new FieldError("registrationDto", "imageFile", "the image file is required"));
		 }

         if (result.hasErrors()) {
            return "registrations/StartRegistration";
        }
        		 //save image file
		 
		 MultipartFile image = registrationDto.getImageFile();
		 Date createdAt = new Date();
		 String storageFileName = createdAt.getTime() + "_" + image.getOriginalFilename();
		 
		 try {
			 String uploadDir = "public/images/";
			 Path uploadPath = Paths.get(uploadDir);
			 
			 if (!Files.exists(uploadPath)) {
				 Files.createDirectories(uploadPath);
			 }
			 try (InputStream inputStream = image.getInputStream()) {
				 Files.copy(inputStream, Paths.get(uploadDir + storageFileName),StandardCopyOption.REPLACE_EXISTING);
			 }
			 
		 } catch (Exception ex) {
			 System.out.println("Execption: " + ex.getMessage());
         }
         Registration registration = new Registration();
		 registration.setName(registrationDto.getName());
         registration.setEmail(registrationDto.getEmail());
		 registration.setCin(registrationDto.getCin());
		 registration.setTel(registrationDto.getTel());
		 registration.setPaymentType(registrationDto.getPaymentType());
		 registration.setCreatedAt(createdAt);
		 registration.setImageFileName(storageFileName);
		 
		 repo.save(registration);




            return "redirect:/registrations";
         }


         @GetMapping("/edit")
         public String showEditPage(
            Model model,
            @RequestParam int id
         ){
            try{
                Registration registration = repo.findById(id).get();
			 model.addAttribute("registration", registration);
			 
			 RegistrationDto registrationDto = new RegistrationDto();
			 registrationDto.setName(registration.getName());
			 registrationDto.setEmail(registration.getEmail());
			 registrationDto.setCin(registration.getCin());
			 registrationDto.setTel(registration.getTel());
			 registrationDto.setPaymentType(registration.getPaymentType());
			 model.addAttribute("registrationDto", registrationDto);

            }
            catch(Exception ex) {
                System.out.println("Exception: " + ex.getMessage());
                return "redirect:/registrations";
            
         }

         return "registrations/EditRegistration";

}
@PostMapping("/edit")
	 public String updateRegistration(
			 Model model,
			 @RequestParam int id,
			 @Valid @ModelAttribute RegistrationDto registrationDto,
			 BindingResult result
             ) {
		 
                try {
                    Registration registration = repo.findById(id).get();
                    model.addAttribute("registration", registration);
                    
                    if (result.hasErrors()) {
                        return "registrations/EditRegistration";
                    }
                    
                    if (!registrationDto.getImageFile().isEmpty()) {
                        //delete old image
                        String uploadDir = "public/images/";
                        Path oldImagePath = Paths.get(uploadDir + registration.getImageFileName());
                        
                        try {
                            Files.delete(oldImagePath);
                            
                        }
                        catch(Exception ex) {
                            System.out.println("Exception: " + ex.getMessage());
                        }
                        
                        //save new image file
                        MultipartFile image = registrationDto.getImageFile();
                        Date createdAt = new Date();
                        String storageFileName = createdAt.getTime() + "_" + image.getOriginalFilename();
                        
                        try (InputStream inputStream = image.getInputStream()){
                            Files.copy(inputStream, Paths.get(uploadDir + storageFileName ),
                                    StandardCopyOption.REPLACE_EXISTING);
                        }
                        registration.setImageFileName(storageFileName);
                        
                    }
                    registration.setName(registrationDto.getName());
                    registration.setEmail(registrationDto.getEmail());
                    registration.setCin(registrationDto.getCin());
                    registration.setTel(registrationDto.getTel());
                    registration.setPaymentType(registrationDto.getPaymentType());
                    
                    
                    repo.save(registration);
                    
                }
                catch(Exception ex) {
                    System.out.println("Exception: " + ex.getMessage());	
                }
                
                
                return "redirect:/registrations";
            }
            @GetMapping("/delete")
	        public String deleteRegistration(
			 
			 @RequestParam int id
			 ) {

                try {
                    Registration registration = repo.findById(id).get();

                    //delete registration image
                    Path imagePath = Paths.get("public/images" + registration.getImageFileName());

                    try {
                        Files.delete(imagePath);
                    }
                    catch (Exception ex){
                        System.out.println("Exception:" + ex.getMessage());
                    }

                    //delete the registration
                    repo.delete(registration);

                }

                catch (Exception ex){
                    System.out.println("Exception:" + ex.getMessage());
                }


                return "redirect:/registrations";
             }


        
        
        
        
        }



