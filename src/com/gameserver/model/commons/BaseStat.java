package com.gameserver.model.commons;

/**
 * @author LEBOC Philippe
 */
public class BaseStat {

    private long maxShield;
    private long maxHealth;
    private long currentShield;
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

    public void setMaxShield(long maxShield) {
        this.maxShield = maxShield;
    }

    public long getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(long maxHealth) {
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
