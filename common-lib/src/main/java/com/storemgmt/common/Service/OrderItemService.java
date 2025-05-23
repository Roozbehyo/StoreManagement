package com.storemgmt.common.Service;

import com.storemgmt.common.Entity.Order;
import com.storemgmt.common.Entity.OrderItem;
import com.storemgmt.common.Entity.Product;
import com.storemgmt.common.Repo.OrderItemRepository;

import java.util.List;

public class OrderItemService implements Service<OrderItem, Integer> {
    @Override
    public void save(OrderItem orderItem) throws Exception {

        Order order = new OrderService().findById(orderItem.getOrder().getId());
        if (order == null) {
            throw new Exception("Order Not Found");
        }

        Product product = new ProductService().findById(orderItem.getProduct().getId());
        if (product == null) {
            throw new Exception("Product Not Found");
        }
        InventoryService inventoryService = new InventoryService();
        inventoryService.calcBalance(order, orderItem);

        try (OrderItemRepository orderItemRepository = new OrderItemRepository()) {
            orderItemRepository.save(orderItem);
        }
    }

    @Override
    public void edit(OrderItem orderItem) throws Exception {
    }

    @Override
    public void remove(Integer id) throws Exception {
    }

    @Override
    public List<OrderItem> findAll() throws Exception {
        try (OrderItemRepository orderItemRepository = new OrderItemRepository()) {
            List<OrderItem> orderItemList = orderItemRepository.findAll();
            if (orderItemList.isEmpty()) {
                throw new Exception("No Items Found");
            }
            return orderItemList;
        }
    }

    public List<OrderItem> findAll(int orderId) throws Exception {
        try (OrderItemRepository orderItemRepository = new OrderItemRepository()) {
            List<OrderItem> orderItemList = orderItemRepository.findAll(orderId);
            if (orderItemList.isEmpty()) {
                throw new Exception("No Items Found");
            }
            return orderItemList;
        }
    }

    @Override
    public OrderItem findById(Integer id) throws Exception {
        try (OrderItemRepository orderItemRepository = new OrderItemRepository()) {
            OrderItem orderItem = orderItemRepository.findById(id);
            if (orderItem == null) {
                throw new Exception("Nothing Found");
            }
            return orderItem;
        }
    }
}
