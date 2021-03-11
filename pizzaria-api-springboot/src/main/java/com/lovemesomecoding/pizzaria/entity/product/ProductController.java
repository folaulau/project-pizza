package com.lovemesomecoding.pizzaria.entity.product;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lovemesomecoding.pizzaria.dto.CustomPage;
import com.lovemesomecoding.pizzaria.dto.EntityDTOMapper;
import com.lovemesomecoding.pizzaria.dto.ProductReadDTO;
import com.lovemesomecoding.pizzaria.exception.ApiException;
import com.lovemesomecoding.pizzaria.utils.ObjMapperUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(value = "products", tags = "Products")
@RequestMapping("/products")
@RestController
public class ProductController {

    private Logger          log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ProductService  productService;

    @Autowired
    private EntityDTOMapper entityDTOMapper;

    @ApiOperation(value = "Get Product By Uuid")
    @GetMapping("/{uuid}")
    public ResponseEntity<ProductReadDTO> getProductByUuid(@ApiParam(name = "uuid", required = true, value = "uuid") @PathVariable("uuid") String uuid) {
        log.debug("getProductByUuid({})", uuid);

        Product product = productService.getByUid(uuid);

        if (product == null) {
            throw new ApiException("Product not found", "Product not found for uuid=" + uuid);
        }

        ProductReadDTO productReadDTO = entityDTOMapper.mapProductToProductReadDTO(product);

        log.debug("productReadDTO: {}", ObjMapperUtils.toJson(productReadDTO));

        return new ResponseEntity<>(productReadDTO, HttpStatus.OK);
    }

    @ApiOperation(value = "Get Products")
    @GetMapping
    public ResponseEntity<CustomPage<ProductReadDTO>> getProducts(Pageable pageable,
            @ApiParam(name = "page", required = false, value = "page", defaultValue = "0") @RequestParam(required = false, name = "page", defaultValue = "0") Integer page,
            @ApiParam(name = "size", required = false, value = "size", defaultValue = "10") @RequestParam(required = false, name = "size", defaultValue = "20") Integer size,
            @ApiParam(name = "sort", required = false, value = "Sorting. format -> sort=attribute:direction&... direction values[asc,desc] i.e /search?sort=type:desc&sort=... ") @RequestParam(required = false, name = "sort") String[] sorts,
            @RequestParam(required = false, name = "types") List<String> types) {
        log.debug("getProducts({})");

        // log.debug("pageable: {}",ObjMapperUtils.toJson(pageable));

        Page<Product> resultPage = this.productService.getByPage(pageable);

        // List<Product> products = result.getContent();
        //
        // log.debug("products: {}",ObjMapperUtils.toJson(products));

        CustomPage<ProductReadDTO> result = new CustomPage<ProductReadDTO>(resultPage);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
