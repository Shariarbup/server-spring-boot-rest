package io.gateways.server.controller;

import io.gateways.server.exception.ApiException;
import io.gateways.server.security.JwtTokenHelper;
import io.gateways.server.utils.JwtAuthRequest;
import io.gateways.server.utils.JwtAuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin(origins = {"http://192.168.55.105:4200"}, methods = {RequestMethod.GET, RequestMethod.POST},maxAge = 3600)
//@CrossOrigin(origins = {"http://localhost:4200"}, methods = {RequestMethod.GET, RequestMethod.POST})
@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private JwtTokenHelper jwtTokenHelper;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> createToken(
            @RequestBody JwtAuthRequest request
            ){
        this.authenticate(request.getUsername(), request.getPassword());
        UserDetails userDetails =this.userDetailsService.loadUserByUsername(request.getUsername());
        String token = this.jwtTokenHelper.generateToken(userDetails);
        JwtAuthResponse response = new JwtAuthResponse();
        response.setToken(token);
        response.setUsername(userDetails.getUsername());
        return new ResponseEntity<JwtAuthResponse>(response, HttpStatus.OK);
    }
    private void authenticate(String username, String password) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        try{
            this.authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        }catch (BadCredentialsException ex){
            System.out.println("Invalid Details");
            throw new ApiException("Invalid username and password ..!");
        }
    }
}
