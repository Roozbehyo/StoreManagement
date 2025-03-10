package com.storemgmt.Model.Service;

import com.storemgmt.Model.Entity.BranchSeller;
import com.storemgmt.Model.Entity.StoreBranch;
import com.storemgmt.Model.Repo.BranchSellerRepository;

import java.util.List;

public class BranchSellerService implements Service<BranchSeller, Integer> {
    @Override
    public void save(BranchSeller branchSeller) throws Exception {

        try (BranchSellerRepository branchSellerRepository = new BranchSellerRepository()) {
            branchSellerRepository.save(branchSeller);
        }
    }

    @Override
    public void edit(BranchSeller branchSeller) throws Exception {
        findById(branchSeller.getId());
        try (BranchSellerRepository branchSellerRepository = new BranchSellerRepository()) {
            branchSellerRepository.edit(branchSeller);
        }
    }

    @Override
    public void remove(Integer branchId) throws Exception {
        findById(branchId);
        try (BranchSellerRepository branchSellerRepository = new BranchSellerRepository()) {
            branchSellerRepository.remove(branchId);

        }
    }

    public void remove(int branchId, int sellerId) throws Exception {
        findById(branchId);
        try (BranchSellerRepository branchSellerRepository = new BranchSellerRepository()) {
            branchSellerRepository.remove(branchId, sellerId);
        }
    }

    @Override
    public List<BranchSeller> findAll() throws Exception {
        return null;
    }

    @Override
    public BranchSeller findById(Integer id) throws Exception {
        return null;
    }

    public List<BranchSeller> findAllByStoreBranchId(Integer storeBranchId) throws Exception {
        StoreBranch storeBranch = new StoreBranchService().findById(storeBranchId);
        if (storeBranch == null) {
            throw new Exception("No Branch Found");
        }
        try (BranchSellerRepository branchSellerRepository = new BranchSellerRepository()) {
            List<BranchSeller> branchSellerList = branchSellerRepository.findAllByStoreBranchId(storeBranchId);
            if (branchSellerList.isEmpty()) {
                throw new Exception("Not Any Seller Found");
            }
            return branchSellerList;
        }
    }
}
