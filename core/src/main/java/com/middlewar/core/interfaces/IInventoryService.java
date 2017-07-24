package com.middlewar.core.interfaces;

import com.middlewar.core.model.Base;
import com.middlewar.core.model.instances.ItemInstance;
import com.middlewar.core.model.inventory.Inventory;
import com.middlewar.core.model.inventory.Resource;

/**
 * @author LEBOC Philippe
 */
public interface IInventoryService {

    /**
     * @param base
     * @param templateId
     * @return
     */
    Resource createNewResource(final Base base, final String templateId);

    /**
     * @param inventory
     * @param templateId
     * @param amount
     * @return
     */
    ItemInstance addItem(final Inventory inventory, final String templateId, final long amount);

    /**
     * @param resource
     * @param amount
     * @return
     */
    boolean addResource(Resource resource, long amount);

    /**
     * @param item
     * @param amount
     * @return
     */
    boolean consumeItem(ItemInstance item, final long amount);

//    24/07/2017 : code used only in tests, so commented because seems to be useless. Delete it later if it's still useless !
//    /**
//     * @param resource
//     * @param amount
//     * @return
//     */
//    boolean consumeResource(Resource resource, final long amount);

    /**
     * @param inventory
     */
    void update(IInventory inventory);

    /**
     * @param base
     */
    void refresh(final Base base);

    /**
     * @param resource
     */
    void refresh(final Resource resource);
}
