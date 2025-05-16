package com.storemgmt.common.Service;

import com.storemgmt.common.Entity.StoreBranch;
import com.storemgmt.common.Repo.StoreBranchRepository;

import java.util.List;

public class StoreBranchService implements Service<StoreBranch, Integer> {
    @Override
    public void save(StoreBranch storeBranch) throws Exception {
        try (StoreBranchRepository storeBranchRepository = new StoreBranchRepository()) {
            storeBranchRepository.save(storeBranch);
        }
    }

    @Override
    public void edit(StoreBranch storeBranch) throws Exception {
        findById(storeBranch.getId());
        try (StoreBranchRepository storeBranchRepository = new StoreBranchRepository()) {
            storeBranchRepository.edit(storeBranch);
        }
    }

    @Override
    public void remove(Integer id) throws Exception {
        findById(id);
        try (StoreBranchRepository storeBranchRepository = new StoreBranchRepository()) {
            storeBranchRepository.remove(id);
            BranchSellerService branchSellerService = new BranchSellerService();
            branchSellerService.remove(id);
        }
    }

    @Override
    public List<StoreBranch> findAll() throws Exception {
        try (StoreBranchRepository storeBranchRepository = new StoreBranchRepository()) {
            List<StoreBranch> storeBranchList = storeBranchRepository.findAll();
            if (storeBranchList.isEmpty()) {
                throw new Exception("No Branch Found");
            }
            return storeBranchList;
        }
    }

    @Override
    public StoreBranch findById(Integer id) throws Exception {
        try (StoreBranchRepository storeBranchRepository = new StoreBranchRepository()) {
            StoreBranch storeBranch = storeBranchRepository.findById(id);
            if (storeBranch == null) {
                throw new Exception("No Branch Found");
            }
            return storeBranch;
        }
    }

    public List<StoreBranch> findByName(String name) throws Exception {
        try (StoreBranchRepository storeBranchRepository = new StoreBranchRepository()) {
            List<StoreBranch> storeBranchList = storeBranchRepository.findByName(name);
            if (storeBranchList.isEmpty()) {
                throw new Exception("No Branch Found");
            }
            return storeBranchList;
        }
    }
}
