package com.ittalents.airbnb.controller;

import com.ittalents.airbnb.model.dto.PhotoDto;
import com.ittalents.airbnb.model.dto.propertyDTOs.*;
import com.ittalents.airbnb.model.dto.propertyDTOs.filters.PropertyCharacteristicsDto;
import com.ittalents.airbnb.model.dto.propertyDTOs.filters.PropertyPriceDto;
import com.ittalents.airbnb.model.repositories.PropertyRepository;
import com.ittalents.airbnb.model.repositories.UserRepository;
import com.ittalents.airbnb.services.PropertyService;
import com.ittalents.airbnb.util.SessionManager;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class PropertyController extends MasterController{

    @Autowired
    private PropertyRepository propertyRepository;
    @Autowired
    private PropertyService propertyService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("properties/add")
    public PropertyCreationDto add(@RequestBody PropertyCreationDto dto, HttpServletRequest request){
         SessionManager.validateLogin(request);
        return propertyService.add(dto, (Long)request.getSession().getAttribute(SessionManager.USER_ID));
    }
    @PutMapping("properties/{pid}/edit")
    public PropertyResponseDto edit(@PathVariable(name = "pid") long pid,@RequestBody PropertyCreationDto dto,HttpServletRequest request){
        SessionManager.validateLogin(request);
        return propertyService.edit(pid,dto,(Long) request.getSession().getAttribute(SessionManager.USER_ID));

    }
    @DeleteMapping("/properties/remove/{pid}")
    public PropertyResponseDto remove(@PathVariable long pid, HttpServletRequest request){
        SessionManager.validateLogin(request);
        return propertyService.remove(pid);
    }
    @PostMapping("/properties/{id}/photo")
    public PhotoDto uploadPhoto(@PathVariable long id, @RequestParam(name = "photo")MultipartFile photo, HttpServletRequest req){
        SessionManager.validateLogin(req);
        return propertyService.uploadPhoto(id, photo);
    }
//    @DeleteMapping("/properties/{pid}/photo/{photoId}")
//    public PhotoDto deletePhoto(@PathVariable long pid, @PathVariable long photoId, HttpServletRequest request){
//        SessionManager.validateLogin(request);
//        return propertyService.deletePhoto(pid, photoId);
//    }

    @DeleteMapping("/properties/{pid}/photo/{photoId}")
    public PhotoDto deletePhoto(HttpServletRequest request, @PathVariable long photoId, @PathVariable long pid) {
        SessionManager.validateLogin(request);
        long uid = (Long) request.getSession().getAttribute(SessionManager.USER_ID);
        return propertyService.deletePhotoById(request, photoId, pid, uid);
    }

    @GetMapping("/users/properties")
    public List<GeneralPropertyResponseDto> getUserProperties(HttpServletRequest request) {
        SessionManager.validateLogin(request);
        return propertyService.getUserProperties((Long) request.getSession().getAttribute(SessionManager.USER_ID));
    }
    @GetMapping("/properties/{id}")
    public GeneralPropertyResponseDto getById(@PathVariable long id){
        return propertyService.getPropertyById(id);
    }

    @GetMapping("/properties")
    public List<GeneralPropertyResponseDto> getAll(){
        return propertyService.findAll();
    }
    @GetMapping("/properties/filter/{typeName}?page={pageIdx}")
    public PageDto filterByType(@PathVariable String typeName, @PathVariable long pageIdx){
       return propertyService.filterByType(typeName, pageIdx);
    }
    @GetMapping(value = "/properties/filter/price?page={pageIdx}")
    public PageDto filterPropertyByPrice(@RequestBody @NonNull PropertyPriceDto filter, @PathVariable long pageIdx) {
        return propertyService.filterByPrice(filter, pageIdx);
    }
    @GetMapping(value = "/properties/filter/characteristics/page={pageIdx}")
    public PageDto filterByCharacteristics(@RequestBody PropertyCharacteristicsDto filter, @PathVariable long pageIdx){
        return propertyService.filterByCharacteristics(filter, pageIdx);
    }


}
