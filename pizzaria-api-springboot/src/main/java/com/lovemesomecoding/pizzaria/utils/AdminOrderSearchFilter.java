package com.lovemesomecoding.pizzaria.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;

import com.lovemesomecoding.pizzaria.exception.ApiException;

public class AdminOrderSearchFilter {

    private Map<String, String> amounts;
    private Map<String, String> sortedBy;

    public AdminOrderSearchFilter() {
        super();
        generateSortBy();
        generateAmounts();
    }

    public Map<String, String> getAmounts() {
        return amounts;
    }

    public void setAmounts(Map<String, String> amounts) {
        this.amounts = amounts;
    }

    public Map<String, String> getSortedBy() {
        return sortedBy;
    }

    public void setSortedBy(Map<String, String> sortedBy) {
        this.sortedBy = sortedBy;
    }

    public AdminOrderSearchFilter generateSortBy() {
        if (sortedBy == null) {
            sortedBy = new HashMap<>();
        }

        sortedBy.put("paidAt", "Paid At");
        sortedBy.put("total", "Total Bill");
        sortedBy.put("delivered", "Delivered");
        sortedBy.put("customerName", "Name");
        sortedBy.put("city", "City");
        return this;
    }

    public AdminOrderSearchFilter generateAmounts() {
        if (amounts == null)
            amounts = new HashMap<>();

        amounts.put("1", "$0 - $200");
        amounts.put("2", "$200 - $400");
        amounts.put("3", "$400 - $600");
        amounts.put("4", "$600 - $800");
        amounts.put("5", "$800 - $1000");
        amounts.put("6", "$1000+");
        return this;
    }

    // ****************** Util Methods *****************

    public static MinMax getFilterAmount(Integer index) {
        MinMax minmax = new MinMax();

        if (index == 1) {
            minmax.setMin(0.0);
            minmax.setMax(200.0);
        } else if (index == 2) {
            minmax.setMin(200.0);
            minmax.setMax(400.0);
        } else if (index == 3) {
            minmax.setMin(400.0);
            minmax.setMax(600.0);
        } else if (index == 4) {
            minmax.setMin(600.0);
            minmax.setMax(800.0);
        } else if (index == 5) {
            minmax.setMin(800.0);
            minmax.setMax(1000.0);
        } else if (index == 6) {
            minmax.setMin(1000.0);
            minmax.setMax(Double.MAX_VALUE);
        }

        return minmax;
    }

    public static boolean validateAmounts(List<Integer> amounts) {
        if (amounts != null && amounts.isEmpty() == false) {
            for (Integer amount : amounts) {
                if (amount <= 0 || amount >= 7) {
                    throw new ApiException("Invalid amount category", "");
                }
            }
            return true;
        }
        return true;
    }

    public static List<Sorting> validateSortBy(String[] sorts) {
        List<Sorting> sortings = new ArrayList<>();
        Iterator<String> it = Arrays.asList(sorts).iterator();

        while (it.hasNext()) {
            String sort = it.next();
            Sorting sorting = new Sorting();
            if (sort != null) {
                String[] sortingDetails = sort.split(":");
                if (sortingDetails != null && sortingDetails.length > 0) {
                    String attribute = sortingDetails[0];

                    if (attribute.equals("paidAt") || attribute.equals("total") || attribute.equals("delivered") || attribute.equals("customerName") || attribute.equals("city")) {
                        sorting.setAttribute(attribute);
                    } else {

                        throw new ApiException("Invalid sorting attribute", "attribute=" + attribute);

                    }
                }

                if (sortingDetails != null && sortingDetails.length > 1) {

                    String direction = sortingDetails[1];

                    if (direction.toLowerCase().equals("asc") || direction.toLowerCase().equals("desc")) {

                        sorting.setDirection(direction);

                    } else {

                        throw new ApiException("Invalid sorting direction", "direction=" + direction);

                    }
                }
                sortings.add(sorting);
            }

        }
        return sortings;
    }
}
