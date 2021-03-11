package com.lovemesomecoding.pizzaria.entity.product;

import java.io.File;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.lovemesomecoding.pizzaria.aws.AwsS3Folder;
import com.lovemesomecoding.pizzaria.aws.AwsS3Service;
import com.lovemesomecoding.pizzaria.utils.RandomGeneratorUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductServiceImp implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AwsS3Service      awsS3Service;

    @Override
    public Product save(Product product) {
        return productRepository.saveAndFlush(product);
    }

    @Override
    public Product update(Product product) {
        return this.save(product);
    }

    @Override
    public List<Product> update(List<Product> products) {
        return productRepository.saveAll(products);
    }

    @Override
    public Product getById(Long id) {
        log.debug("getById({})", id);
        return this.productRepository.findById(id).orElse(null);
    }

    @Override
    public Product getByUid(String uid) {
        log.debug("getByUid({})", uid);
        return productRepository.findByUuid(uid);
    }

    @Override
    public Page<Product> getByPage(Pageable page) {
        log.debug("getByPage(..)");
        return productRepository.findByActive(true, page);
    }

    @Override
    public String uploadProductProfileImg(File file) {
        String key = AwsS3Folder.PIZZARIA_PRODUCT + "/" + RandomGeneratorUtils.getS3FileKey(file.getName());
        return awsS3Service.uploadFile(key, file);
    }

}
