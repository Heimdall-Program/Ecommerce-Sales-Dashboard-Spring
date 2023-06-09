package com.example.MonitoringInternetShop.Controllers;

import com.example.MonitoringInternetShop.Models.Order;
import com.example.MonitoringInternetShop.Models.OrderItem;
import com.example.MonitoringInternetShop.Models.Product;
import com.example.MonitoringInternetShop.Services.OrderService;
import com.example.MonitoringInternetShop.Services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class DashboardController {
    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        List<Product> topProducts = productService.getTopProducts();
        List<Order> latestOrders = orderService.getLatestOrders();

        model.addAttribute("topProducts", topProducts);
        model.addAttribute("latestOrders", latestOrders);

        int totalSoldProducts = latestOrders.stream()
                .flatMap(order -> order.getOrderItems().stream())
                .mapToInt(OrderItem::getQuantity)
                .sum();

        BigDecimal totalRevenue = latestOrders.stream()
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal averageCheck = latestOrders.size() > 0
                ? totalRevenue.divide(BigDecimal.valueOf(latestOrders.size()), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        List<Map<String, Object>> salesData = topProducts.stream()
                .map(product -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("name", product.getName());
                    map.put("sales", product.getSales());
                    map.put("revenue", product.getPrice().multiply(new BigDecimal(product.getSales()))); // new line
                    return map;
                })
                .collect(Collectors.toList());

        model.addAttribute("totalSoldProducts", totalSoldProducts);
        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("averageCheck", averageCheck);
        model.addAttribute("salesData", salesData);

        return "dashboard";
    }

}
