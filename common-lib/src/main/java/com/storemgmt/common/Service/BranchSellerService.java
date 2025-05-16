package com.storemgmt.common.Service;

import com.storemgmt.common.Entity.BranchSeller;
import com.storemgmt.common.Entity.StoreBranch;
import com.storemgmt.common.Repo.BranchSellerRepository;
import lombok.extern.log4j.Log4j;

import java.util.List;

@Log4j
public class BranchSellerService implements Service<BranchSeller, Integer> {
    @Override
    public void save(BranchSeller branchSeller) throws Exception {
        BranchSeller result = findById(branchSeller.getSeller().getId());
        if (result != null) {
            throw new Exception("Seller Already Assigned");
        }
        try (BranchSellerRepository branchSellerRepository = new BranchSellerRepository()) {
            branchSellerRepository.save(branchSeller);
        }
    }

    @Override
    public void edit(BranchSeller branchSeller) throws Exception {
        findById(branchSeller.getStoreBranch().getId(), branchSeller.getSeller().getId());
        try (BranchSellerRepository branchSellerRepository = new BranchSellerRepository()) {
            branchSellerRepository.edit(branchSeller);
        }
    }

    @Override
    public void remove(Integer branchId) throws Exception {
        StoreBranch storeBranch = new StoreBranchService().findById(branchId);
        if (storeBranch == null) {
            throw new Exception("No Branch Found");
        }
        try (BranchSellerRepository branchSellerRepository = new BranchSellerRepository()) {
            branchSellerRepository.remove(branchId);

        }
    }

    public void remove(int branchId, int sellerId) throws Exception {
        findById(branchId, sellerId);
        try (BranchSellerRepository branchSellerRepository = new BranchSellerRepository()) {
            branchSellerRepository.remove(branchId, sellerId);
        }
    }

    @Override
    public List<BranchSeller> findAll() throws Exception {
        return null;
    }

    @Override
    public BranchSeller findById(Integer sellerId) throws Exception {
        try (BranchSellerRepository branchSellerRepository = new BranchSellerRepository()) {
            BranchSeller branchSeller = branchSellerRepository.findById(sellerId);
            if (branchSeller == null) {
                log.info("No Seller Found");
                return null;
            }
            return branchSeller;
        }
    }

    public BranchSeller findById(int storeBranchId, int sellerId) throws Exception {
        try (BranchSellerRepository branchSellerRepository = new BranchSellerRepository()) {
            BranchSeller branchSeller = branchSellerRepository.findById(storeBranchId, sellerId);
            if (branchSeller == null) {
                throw new Exception("Nothing Found");
            }
            return branchSeller;
        }
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
