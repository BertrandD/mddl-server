package com.middlewar.api.services.impl;

import com.middlewar.api.dao.BaseInventoryDao;
import com.middlewar.api.services.BaseInventoryService;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.inventory.BaseInventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service not used but we will keep it. If in few months we dont use, we will drop it
 *
 * @author LEBOC Philippe
 */
@Service
public class BaseInventoryServiceImpl extends DefaultServiceImpl<BaseInventory, BaseInventoryDao> implements BaseInventoryService {

    @Autowired
    private BaseInventoryDao baseInventoryDao;

    @Override
    public BaseInventory create(Base base) {
        return baseInventoryDao.save(new BaseInventory(base));
    }
}
