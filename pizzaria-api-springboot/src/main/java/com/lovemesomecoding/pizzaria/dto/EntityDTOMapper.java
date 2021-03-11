package com.lovemesomecoding.pizzaria.dto;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

import com.lovemesomecoding.pizzaria.entity.address.Address;
import com.lovemesomecoding.pizzaria.entity.order.Order;
import com.lovemesomecoding.pizzaria.entity.order.lineitem.LineItem;
import com.lovemesomecoding.pizzaria.entity.product.Product;
import com.lovemesomecoding.pizzaria.entity.user.User;
import com.lovemesomecoding.pizzaria.entity.user.session.UserSession;
import com.lovemesomecoding.pizzaria.paymentmethod.OrderPaymentMethod;
import com.lovemesomecoding.pizzaria.paymentmethod.PaymentMethod;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EntityDTOMapper {

    User mapSignUpDTOToUser(SignUpDTO signUpDTO);

    User mapUserUpdateDTOToUser(UserUpdateDTO userUpdateDTO);

    @Mappings({@Mapping(target = "id", ignore = true), @Mapping(target = "uuid", ignore = true), @Mapping(target = "address", ignore = true)})
    User patchUpdateUser(UserUpdateDTO userUpdateDTO, @MappingTarget User user);

    UserDTO mapUserToUserDTO(User user);

    AuthenticationResponseDTO mapUserToUserAuthSuccessDTO(User user);

    UserSessionDTO mapUserSessionToUserSessionDTO(UserSession userSession);

    List<UserSessionDTO> mapUserSessionsToUserSessionDTOs(List<UserSession> userSessions);

//========================= Product =====================
    
    Product mapProductCreateDTOToProduct(ProductCreateDTO productCreateDTO); 
    
    Product mapProductUpdateDTOToProduct(ProductUpdateDTO productUpdateDTO); 
    
    ProductReadDTO mapProductToProductReadDTO(Product product); 
    
    List<ProductReadDTO> mapProductsToProductReadDTOs(List<Product> product);
    
    @Mappings({ 
        @Mapping(target = "id", ignore = true), 
        @Mapping(target = "uuid", ignore = true) 
    })
    Product patchUpdateProduct(ProductUpdateDTO productUpdateDTO, @MappingTarget Product product);
    
    //========================= Payment Method =====================
    
    PaymentMethod mapCardPMCreateDTOToPaymentMethod(CardPMCreateDTO cardPMCreateDTO);
    
    List<PaymentMethodReadDTO> mapPaymentMethodsToPaymentMethodReadDTOs(List<PaymentMethod> paymentMethods);
    
    //========================= Order =====================
    
    Order mapOrderCreateDTOToOrder(OrderCreateDTO orderCreateDTO);
    
    OrderReadDTO mapOrderToOrderReadDTO(Order order);
    
    OrderPaymentMethod mapPaymentMethodToOrderPaymentMethod(PaymentMethod paymentMethod);
    
//    @Mappings(value= {
//            @Mapping(target = "token", source = "token"),
//            @Mapping(target = "userUid", source = "user.uid")
//    })
//    SessionDTO mapUserAndTokenToSessionDTO(User user, String token);
    
    LineItemDTO mapLineItemToLineItemDTO(LineItem lineItem);

    LineItem mapLineItemDTOToLineItem(LineItemDTO lineItemDTO);

    Address mapAddressDTOToAddress(AddressDTO addressDTO);

}
