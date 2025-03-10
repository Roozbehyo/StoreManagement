package com.storemgmt.Model.Service;

import com.storemgmt.Model.Entity.Seller;
import com.storemgmt.Model.Repo.SellerRepository;

import java.util.List;

public class SellerService implements Service<Seller, Integer> {
    @Override
    public void save(Seller seller) throws Exception {
        if (checkUsernameExistence(seller.getUsername())) {
            throw new Exception("Username is already taken");
        }
        try (SellerRepository sellerRepository = new SellerRepository()) {
            sellerRepository.save(seller);
        }
    }

    @Override
    public void edit(Seller seller) throws Exception {
        findById(seller.getId());
        try (SellerRepository sellerRepository = new SellerRepository()) {
            sellerRepository.edit(seller);
        }
    }

    @Override
    public void remove(Integer id) throws Exception {
    }

    @Override
    public List<Seller> findAll() throws Exception {
        try (SellerRepository sellerRepository = new SellerRepository()) {
            List<Seller> sellerList = sellerRepository.findAll();
            if (sellerList.isEmpty()) {
                throw new Exception("Seller not found");
            }
            return sellerList;
        }
    }

    @Override
    public Seller findById(Integer id) throws Exception {
        try (SellerRepository sellerRepository = new SellerRepository()) {
            Seller seller = sellerRepository.findById(id);
            if (seller == null) {
                throw new Exception("Seller not found");
            }
            return seller;
        }
    }

    public boolean checkUsernameExistence(String username) throws Exception {
        try (SellerRepository sellerRepository = new SellerRepository()) {
            Integer result = sellerRepository.findIdByUsername(username);
            return result != null;
        }
    }

    public List<Seller> findAllByFNameAndLName(String firstname, String lastname) throws Exception {
        try (SellerRepository sellerRepository = new SellerRepository()) {
            List<Seller> sellerList = sellerRepository.findAllByFNameAndLName(firstname, lastname);
            if (sellerList.isEmpty()) {
                throw new Exception("Seller not found");
            }
            return sellerList;
        }
    }

    public Seller findByUsernameAndPassword(String username, String password) throws Exception {
        try (SellerRepository sellerRepository = new SellerRepository()) {
            Seller seller = sellerRepository.findByUsernameAndPassword(username, password);
            if (seller == null) {
                throw new Exception("Access Denied !!! Username or Password Incorrect");
            }
            return seller;
        }
    }
}
