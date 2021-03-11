package com.lovemesomecoding.pizzaria.data_loader;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.lovemesomecoding.pizzaria.entity.product.Product;
import com.lovemesomecoding.pizzaria.entity.product.ProductCategory;
import com.lovemesomecoding.pizzaria.entity.product.ProductRepository;
import com.lovemesomecoding.pizzaria.entity.product.ProductType;
import com.lovemesomecoding.pizzaria.entity.product.deal.Deal;
import com.lovemesomecoding.pizzaria.entity.product.deal.DealRepository;

@Profile({"local"})
public class ProductLoader implements CommandLineRunner{

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private DealRepository    dealRepository;

    @Override
    public void run(String... args) throws Exception {
        loadPizza();

        loadChickend();

        loadDrink();

        loadDeals();
    }

    private void loadPizza() {

        Product product = new Product();
        product.setName("Cheese Pizza");
        product.setId(1L);
        product.setCategory(ProductCategory.PIZZA_CHEESE);
        product.setType(ProductType.PIZZA);
        product.setPrice(5.0);
        product.setRating(2);
        product.setActive(true);

        productRepository.saveAndFlush(product);

        product = new Product();
        product.setName("Chicken Pizza");
        product.setId(2L);
        product.setCategory(ProductCategory.PIZZA_CHICKEN);
        product.setType(ProductType.PIZZA);
        product.setPrice(5.0);
        product.setRating(3);
        product.setActive(true);

        productRepository.saveAndFlush(product);

        product = new Product();
        product.setName("Hawaiian Pizza");
        product.setId(3L);
        product.setCategory(ProductCategory.PIZZA_HAWAII);
        product.setType(ProductType.PIZZA);
        product.setPrice(7.50);
        product.setRating(3);
        product.setActive(true);

        productRepository.saveAndFlush(product);

        product = new Product();
        product.setName("Meat Lover Pizza");
        product.setId(4L);
        product.setCategory(ProductCategory.PIZZA_MEAT_LOVER);
        product.setType(ProductType.PIZZA);
        product.setPrice(7.50);
        product.setRating(3);
        product.setActive(true);

        productRepository.saveAndFlush(product);

        product = new Product();
        product.setName("Pepperone Pizza");
        product.setId(5L);
        product.setCategory(ProductCategory.PIZZA_PEPPERONI);
        product.setType(ProductType.PIZZA);
        product.setPrice(5.0);
        product.setRating(2);
        product.setActive(true);

        productRepository.saveAndFlush(product);

        product = new Product();
        product.setName("Veggie Pizza");
        product.setId(6L);
        product.setCategory(ProductCategory.PIZZA_VEGGIE);
        product.setType(ProductType.PIZZA);
        product.setPrice(7.50);
        product.setRating(3);
        product.setActive(true);

        productRepository.saveAndFlush(product);

    }

    private void loadChickend() {

        Product product = new Product();
        product.setName("Chicken Wings");
        product.setId(7L);
        product.setCategory(ProductCategory.CHICKEN_WINGS);
        product.setType(ProductType.CHICKEN);
        product.setActive(true);
        product.setPrice(8.0);
        product.setRating(4);
        productRepository.saveAndFlush(product);

        product = new Product();
        product.setName("Boneless Chicken");
        product.setId(8L);
        product.setCategory(ProductCategory.CHICKEN_BONELESS);
        product.setType(ProductType.CHICKEN);
        product.setActive(true);
        product.setPrice(8.0);
        product.setRating(4);
        productRepository.saveAndFlush(product);

        product = new Product();
        product.setName("Friend Chicken");
        product.setId(9L);
        product.setCategory(ProductCategory.CHICKEN_FRIED);
        product.setType(ProductType.CHICKEN);
        product.setActive(true);
        product.setPrice(10.0);
        product.setRating(4);
        productRepository.saveAndFlush(product);

    }

    private void loadDrink() {

        Product product = new Product();
        product.setName("Fanta");
        product.setId(10L);
        product.setCategory(ProductCategory.DRINK_FANTA);
        product.setType(ProductType.DRINK);
        product.setPrice(1.50);
        product.setRating(3);
        product.setActive(true);

        productRepository.saveAndFlush(product);

        product = new Product();
        product.setName("Gatorade");
        product.setId(11L);
        product.setCategory(ProductCategory.DRINK_GATORADE);
        product.setType(ProductType.DRINK);
        product.setPrice(1.50);
        product.setRating(3);
        product.setActive(true);

        productRepository.saveAndFlush(product);

        product = new Product();
        product.setName("Juice");
        product.setId(12L);
        product.setCategory(ProductCategory.DRINK_JUICE);
        product.setType(ProductType.DRINK);
        product.setPrice(1.50);
        product.setRating(3);
        product.setActive(true);

        productRepository.saveAndFlush(product);

        product = new Product();
        product.setName("Naked");
        product.setId(13L);
        product.setCategory(ProductCategory.DRINK_NAKED);
        product.setType(ProductType.DRINK);
        product.setPrice(1.50);
        product.setRating(3);
        product.setActive(true);

        productRepository.saveAndFlush(product);

        product = new Product();
        product.setName("Pepsi");
        product.setId(14L);
        product.setCategory(ProductCategory.DRINK_PEPSI);
        product.setType(ProductType.DRINK);
        product.setPrice(1.50);
        product.setRating(3);
        product.setActive(true);

        productRepository.saveAndFlush(product);

        product = new Product();
        product.setName("Sprite");
        product.setId(15L);
        product.setCategory(ProductCategory.DRINK_SPRITE);
        product.setType(ProductType.DRINK);
        product.setPrice(1.50);
        product.setRating(3);
        product.setActive(true);

        productRepository.saveAndFlush(product);
    }

    private void loadDeals() {
        Deal deal = new Deal();
        deal.setName("Tastemaker");
        deal.setId(1L);
        deal.setCost(10.0);
        deal.setRating(100);
        deal.setActive(true);
        deal.addProduct(new Product(5L));
        dealRepository.saveAndFlush(deal);
        
        
        deal = new Deal();
        deal.setName("Big Dinner Box");
        deal.setId(2L);
        deal.setCost(10.0);
        deal.setRating(90);
        deal.setActive(true);
        deal.addProduct(new Product(1L));
        deal.addProduct(new Product(5L));
        dealRepository.saveAndFlush(deal);
        
        deal = new Deal();
        deal.setName("Veggie Lover");
        deal.setId(3L);
        deal.setCost(10.0);
        deal.setRating(80);
        deal.setActive(true);
        deal.addProduct(new Product(6L));
        dealRepository.saveAndFlush(deal);
    }



}
