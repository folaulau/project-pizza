package com.lovemesomecoding.pizzaria.entity.product;

import java.util.ArrayList;
import java.util.List;

public enum ProductCategory {

    DRINK_SPRITE,
    DRINK_FANTA,
    DRINK_PEPSI,
    DRINK_JUICE,
    DRINK_GATORADE,
    DRINK_NAKED,
    PIZZA_CHEESE,
    PIZZA_PEPPERONI,
    PIZZA_VEGGIE,
    PIZZA_CHICKEN,
    PIZZA_HAWAII,
    PIZZA_MEAT_LOVER,
    CHICKEN_WINGS,
    CHICKEN_BONELESS,
    CHICKEN_FRIED;
    
    
    public static List<ProductCategory> getPizzaCategories(){
        List<ProductCategory> categories = new ArrayList<ProductCategory>();
        categories.add(PIZZA_CHEESE);
        categories.add(PIZZA_PEPPERONI);
        categories.add(PIZZA_VEGGIE);
        categories.add(PIZZA_CHICKEN);
        categories.add(PIZZA_HAWAII);
        categories.add(PIZZA_MEAT_LOVER);
        
        return categories;
    }
    
    public static List<ProductCategory> getDrinkCategories(){
        List<ProductCategory> categories = new ArrayList<ProductCategory>();
        categories.add(DRINK_SPRITE);
        categories.add(DRINK_FANTA);
        categories.add(DRINK_PEPSI);
        categories.add(DRINK_JUICE);
        categories.add(DRINK_GATORADE);
        categories.add(DRINK_NAKED);
        
        return categories;
    }
    
    public static List<ProductCategory> getChickenCategories(){
        List<ProductCategory> categories = new ArrayList<ProductCategory>();
        categories.add(CHICKEN_WINGS);
        categories.add(CHICKEN_BONELESS);
        categories.add(CHICKEN_FRIED);
        
        return categories;
    }
}
