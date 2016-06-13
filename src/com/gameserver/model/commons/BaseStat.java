package com.gameserver.model.commons;

import com.fasterxml.jackson.annotation.JsonView;
import com.util.data.json.View;

/**
 * @author LEBOC Philippe
 */
public class BaseStat {

    @JsonView(View.Standard.class)
    private long maxShield;

    @JsonView(View.Standard.class)
    private long maxHealth;

    @JsonView(View.Standard.class)
    private long currentShield;

    @JsonView(View.Standard.class)
    private long currentHealth;

    public BaseStat(final long maxShield, final long maxHealth) {
        setMaxShield(maxShield);
        setMaxHealth(maxHealth);
        setCurrentHealth(getMaxHealth());
        setCurrentShield(getMaxShield());
    }

    public long getMaxShield() {
        return maxShield;
    }

    private void setMaxShield(long maxShield) {
        this.maxShield = maxShield;
    }

    public long getMaxHealth() {
        return maxHealth;
    }

    private void setMaxHealth(long maxHealth) {
        this.maxHealth = maxHealth;
    }

    public long getCurrentShield() {
        return currentShield;
    }

    public void setCurrentShield(long currentShield) {
        this.currentShield = currentShield;
    }

    public long getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(long currentHealth) {
        this.currentHealth = currentHealth;
    }
}
