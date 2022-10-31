package com.ittalents.airbnb.controller;

import com.ittalents.airbnb.model.dto.propertyDTOs.GeneralPropertyResponseDto;
import com.ittalents.airbnb.model.dto.propertyDTOs.PagePropertyDto;
import com.ittalents.airbnb.model.dto.propertyDTOs.PropertyCreationDto;
import com.ittalents.airbnb.services.WishlistService;
import com.ittalents.airbnb.util.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
@RestController
public class WishlistController extends MasterController{

    @Autowired
    WishlistService wishlistService;

    @PostMapping("/properties/{id}/wishlist")
    public GeneralPropertyResponseDto addToWishlist(@PathVariable long id, HttpServletRequest request){
        SessionManager.validateLogin(request);
        return wishlistService.addToWishlist(id, (Long)request.getSession().getAttribute(SessionManager.USER_ID));
    }

    @DeleteMapping("/properties/{id}/wishlist")
    public PagePropertyDto removeFromWishlist(@PathVariable long id, HttpServletRequest request){
        SessionManager.validateLogin(request);
        return wishlistService.removeFromWishlist(id, (Long)request.getSession().getAttribute(SessionManager.USER_ID));
    }

    @GetMapping("/users/wishlist")
    public List<PagePropertyDto> getAllFromWishlist(HttpServletRequest request){
        SessionManager.validateLogin(request);
        return wishlistService.getAllFromWishlist((Long) request.getSession().getAttribute(SessionManager.USER_ID));
    }
}
