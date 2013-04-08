package dk.eazyit.eazyregnskab.domain;

/**
 * @author
 */
public enum Country {

    DK(MoneyCurrency.DKK);

    MoneyCurrency moneyCurrency;

    private Country(MoneyCurrency moneyCurrency) {
        this.moneyCurrency = moneyCurrency;
    }

    public MoneyCurrency getMoneyCurrency() {
        return moneyCurrency;
    }

    public void setMoneyCurrency(MoneyCurrency moneyCurrency) {
        this.moneyCurrency = moneyCurrency;
    }
}
