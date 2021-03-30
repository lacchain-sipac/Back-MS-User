package com.invest.honduras.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.invest.honduras.http.request.UserCreateRequest;
import com.invest.honduras.http.request.UserForgetPasswordRequest;
import com.invest.honduras.http.request.UserPasswordUpdateRequest;
import com.invest.honduras.http.request.UserStateUpdateRequest;
import com.invest.honduras.http.request.UserUpdateRequest;
import com.invest.honduras.http.response.CompanyFindResponse;
import com.invest.honduras.http.response.UserDidResponse;
import com.invest.honduras.http.response.UserFindAllResponse;
import com.invest.honduras.http.response.UserGeneralResponse;
import com.invest.honduras.http.response.UserProfileResponse;
import com.invest.honduras.http.response.UserResponse;
import com.invest.honduras.http.response.UserTokenResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;
import reactor.core.publisher.Mono;


@Api(value="MS-USER", description="Microservicio de Gestion de Usuarios de la aplicacion SIPAC")
public interface ApiUserController {
	
	@ApiOperation(
            value = "Interfaz API para listar todos los usuarios de la aplicacion SIPAC.",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE, 
            response = UserFindAllResponse.class,
            httpMethod = "GET" 
            )

    @ApiResponses(value = {    		
    		 @ApiResponse(code = 200, message = "Exito al obtener la lista de usuario" , response = UserFindAllResponse.class ),
             @ApiResponse(code = 401, message = "No autorizado para obtener el recurso"),
             @ApiResponse(code = 403, message = "El acceso al recurso esta prohibido"),
             @ApiResponse(code = 404, message = " El recurso no pudo ser encontrado")
    }
    		)	
	Mono<ResponseEntity<UserFindAllResponse>> findAllUser();

	
	@ApiOperation(
            value = "Interfaz API para listar todas las compañias  de la aplicacion SIPAC.",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE, 
            response = CompanyFindResponse.class,
            httpMethod = "GET" 
            )
	
    @ApiResponses(value = {    		
   		 @ApiResponse(code = 200, message = "Exito al obtener la lista de compañias" ,response = CompanyFindResponse.class),
            @ApiResponse(code = 401, message = "No autorizado para obtener el recurso"),
            @ApiResponse(code = 403, message = "El acceso al recurso esta prohibido"),
            @ApiResponse(code = 404, message = " El recurso no pudo ser encontrado")
   }
   		)
	
	Mono<ResponseEntity<CompanyFindResponse>> findCompany();

	
	@ApiOperation(
            value = "Interfaz API para agregar un usuario en la aplicacion SIPAC",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE, 
            response = UserResponse.class,
            httpMethod = "POST" 
            )
    @ApiResponses(value = {    		
      		 @ApiResponse(code = 201, message = "Exito al crear el usuario" , response = UserResponse.class),
               @ApiResponse(code = 401, message = "No autorizado para obtener el recurso"),
               @ApiResponse(code = 403, message = "El acceso al recurso esta prohibido"),
               @ApiResponse(code = 404, message = " El recurso no pudo ser encontrado")
      }
      		)
	
	public Mono<ResponseEntity<UserResponse>> addUser( @RequestHeader("Authorization") String token 
			, @RequestBody UserCreateRequest userRequest) throws Exception;
	
	
    @ApiOperation(
            value = " Interfaz API para actualizar los datos de un usuario en la aplicacion SIPAC.",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE, 
            response = UserResponse.class,
            httpMethod = "PUT" 
            )

    @ApiResponses(value = {    		
     		 @ApiResponse(code = 200, message = "Exito al actualizar el usuario" , response = UserResponse.class),
              @ApiResponse(code = 401, message = "No autorizado para obtener el recurso"),
              @ApiResponse(code = 403, message = "El acceso al recurso esta prohibido"),
              @ApiResponse(code = 404, message = " El recurso no pudo ser encontrado")
     }
     		)    
    Mono<ResponseEntity<UserResponse>> updateUser(@PathVariable(value = "id") String id,@RequestBody UserUpdateRequest user,
    		@RequestHeader("Authorization") String token );
    

    @ApiOperation(
            value = " Interfaz API para buscar un usuario en la aplicacion SIPAC.",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE, response = UserResponse.class,
            httpMethod = "GET" 
            )
    
    @ApiResponses(value = {    		
    		 @ApiResponse(code = 200, message = "Exito al obtener el usuario" , response = UserResponse.class),
             @ApiResponse(code = 401, message = "No autorizado para obtener el recurso"),
             @ApiResponse(code = 403, message = "El acceso al recurso esta prohibido"),
             @ApiResponse(code = 404, message = " El recurso no pudo ser encontrado")
    }
    		)
    
    Mono<ResponseEntity<UserResponse>> findByIdUser(@PathVariable(value = "id") String id);

    @ApiOperation(
            value = "Interfaz API para listar el perfil de un usuario en la aplicacion SIPAC.",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE, response = UserProfileResponse.class,
            httpMethod = "GET" 
            )
    @ApiResponses(value = {    		
   		 @ApiResponse(code = 200, message = "Exito al obtener el usuario" , response = UserProfileResponse.class),
            @ApiResponse(code = 401, message = "No autorizado para obtener el recurso"),
            @ApiResponse(code = 403, message = "El acceso al recurso esta prohibido"),
            @ApiResponse(code = 404, message = " El recurso no pudo ser encontrado")
   }
   		)

    
    public Mono<ResponseEntity<UserProfileResponse>> getProfile(String id) ;

    
    @ApiOperation(
            value = "Interfaz API para actualizar una contraseña en la aplicacion SIPAC.",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE, 
            response = UserGeneralResponse.class,
            httpMethod = "PUT" 
            )
    
    @ApiResponses(value = {    		
   		 @ApiResponse(code = 200, message = "Exito al actualizar el password de usuario" , response = UserGeneralResponse.class),
            @ApiResponse(code = 401, message = "No autorizado para obtener el recurso"),
            @ApiResponse(code = 403, message = "El acceso al recurso esta prohibido"),
            @ApiResponse(code = 404, message = " El recurso no pudo ser encontrado")
   }
   		)

    
    Mono<ResponseEntity<UserGeneralResponse>> updatePasswordUser(@PathVariable(value = "id") String id,
			@RequestBody UserPasswordUpdateRequest userPasswordUpdateRequest,
			@RequestHeader("Authorization") String token ); 
            
 
    
    @ApiOperation(
            value = "Interfaz API para recuperar una contraseña en la aplicacion SIPAC",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE, 
            response = UserGeneralResponse.class,
            httpMethod = "POST" 
            )

    @ApiResponses(value = {    		
      		 @ApiResponse(code = 200, message = "Exito al enviar el password de usuario para su recuperacion",  response = UserGeneralResponse.class),
               @ApiResponse(code = 401, message = "No autorizado para obtener el recurso"),
               @ApiResponse(code = 403, message = "El acceso al recurso esta prohibido"),
               @ApiResponse(code = 404, message = " El recurso no pudo ser encontrado")
      }
      		)
    
    Mono<ResponseEntity<UserGeneralResponse>> forgetPasswordUser(
			@RequestBody UserForgetPasswordRequest userForgetPasswordRequest); 

    @ApiOperation(
            value = "Interfaz API para actualizar el estado de un usuario en la aplicacion SIPAC.",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE, 
            response = UserGeneralResponse.class,
            httpMethod = "PUT" 
            )

    @ApiResponses(value = {    		
     		 @ApiResponse(code = 200, message = "Exito al actualizar el estado de usuario" , response = UserGeneralResponse.class ),
              @ApiResponse(code = 401, message = "No autorizado para obtener el recurso"),
              @ApiResponse(code = 403, message = "El acceso al recurso esta prohibido"),
              @ApiResponse(code = 404, message = " El recurso no pudo ser encontrado")
     }
     		)

    
    Mono<ResponseEntity<UserGeneralResponse>> updateStateUser(@PathVariable(value = "id") String id,
			@RequestBody UserStateUpdateRequest userStateUpdateRequest ,
			@RequestHeader("Authorization") String token); 
    
    @ApiOperation(
            value = "Interfaz API para obtener un token uuid",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE, response = UserTokenResponse.class,
            httpMethod = "GET" 
            )
    @ApiResponses(value = {    		
    		 @ApiResponse(code = 200, message = "Exito al obtener codigo token" , response = UserTokenResponse.class ),
             @ApiResponse(code = 401, message = "No autorizado para obtener el recurso"),
             @ApiResponse(code = 403, message = "El acceso al recurso esta prohibido"),
             @ApiResponse(code = 404, message = " El recurso no pudo ser encontrado")
    }
    		)
    
    Mono<ResponseEntity<UserTokenResponse>> getToken(@RequestHeader("Authorization") String token);
    
    
    @ApiOperation(
            value = "Interfaz API para actualizar el did User",
    	    produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE, response = UserDidResponse.class,
            httpMethod = "GET" 
            )
    @ApiResponses(value = {    		
   		 @ApiResponse(code = 200, message = "Exito al actualizar el did de usuario" , response = UserDidResponse.class ),
            @ApiResponse(code = 401, message = "No autorizado para obtener el recurso"),
            @ApiResponse(code = 403, message = "El acceso al recurso esta prohibido"),
            @ApiResponse(code = 404, message = " El recurso no pudo ser encontrado")
   }
   		)
    
    Mono<ResponseEntity<UserDidResponse>> updateDidUserJWT(@RequestParam(value = "access_token") String accessToken,
			@RequestParam(value = "state") String state);
           
    
}
