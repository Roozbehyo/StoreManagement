package com.storemgmt.common.Service;

import com.storemgmt.common.Entity.Customer;
import com.storemgmt.common.Entity.Order;
import com.storemgmt.common.Entity.Seller;
import com.storemgmt.common.Entity.StoreBranch;
import com.storemgmt.common.Repo.OrderRepository;

import java.time.LocalDate;
import java.util.List;

public class OrderService implements Service<Order, Integer> {
    @Override
    public void save(Order order) throws Exception {
        if (!(order.getOrderDate().getYear() >= 1980 && order.getOrderDate().getYear() <= LocalDate.now().getYear())) {
            throw new Exception("Invalid Order Year");
        }
        Customer customer = new CustomerService().findById(order.getCustomer().getId());
        if (customer == null) {
            throw new Exception("Customer Not Found");
        }
        StoreBranch storeBranch = new StoreBranchService().findById(order.getStoreBranch().getId());
        if (storeBranch == null) {
            throw new Exception("Store Branch Not Found");
        }
        Seller seller = new SellerService().findById(order.getSeller().getId());
        if (seller == null) {
            throw new Exception("Seller Not Found");
        }

        try (OrderRepository orderRepository = new OrderRepository()) {
            orderRepository.save(order);
        }
    }

    @Override
    public void edit(Order order) throws Exception {
        findById(order.getId());
        Seller seller = new SellerService().findById(order.getSeller().getId());
        if (seller == null) {
            throw new Exception("Seller Not Found");
        }
        try (OrderRepository orderRepository = new OrderRepository()) {
            orderRepository.edit(order);
        }
    }

    @Override
    public void remove(Integer id) throws Exception {
    }

    @Override
    public List<Order> findAll() throws Exception {
        try (OrderRepository orderRepository = new OrderRepository()) {
            List<Order> orderList = orderRepository.findAll();
            if (orderList.isEmpty()) {
                throw new Exception("Order not found");
            }
            return orderList;
        }
    }

    @Override
    public Order findById(Integer id) throws Exception {
        try (OrderRepository orderRepository = new OrderRepository()) {
            Order order = orderRepository.findById(id);
            if (order == null) {
                throw new Exception("Order not found");
            }
            return order;
        }
    }

    public List<Order> findAllByCustomerFNameAndLName(String firstname, String lastname) throws Exception {
        try (OrderRepository orderRepository = new OrderRepository()) {
            List<Order> orderList = orderRepository.findAllByCustomerFNameAndLName(firstname, lastname);
            if (orderList.isEmpty()) {
                throw new Exception("Order not found");
            }
            return orderList;
        }
    }

    public List<Order> findAllBySellerFNameAndLName(String firstname, String lastname) throws Exception {
        try (OrderRepository orderRepository = new OrderRepository()) {
            List<Order> orderList = orderRepository.findAllBySellerFNameAndLName(firstname, lastname);
            if (orderList.isEmpty()) {
                throw new Exception("Order not found");
            }
            return orderList;
        }
    }
}
