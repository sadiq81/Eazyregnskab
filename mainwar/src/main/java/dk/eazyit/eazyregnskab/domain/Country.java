package dk.eazyit.eazyregnskab.domain;

/**
 * @author https://en.wikipedia.org/wiki/ISO_3166-1
 */

public enum Country {

    DNK(MoneyCurrency.DKK),
    AUS(MoneyCurrency.EUR),
    BEL(MoneyCurrency.EUR),
    CYP(MoneyCurrency.EUR),
    DEU(MoneyCurrency.EUR),
    EST(MoneyCurrency.EUR),
    ESP(MoneyCurrency.EUR),
    FIN(MoneyCurrency.EUR),
    FRA(MoneyCurrency.EUR),
    GRC(MoneyCurrency.EUR),
    IRL(MoneyCurrency.EUR),
    ITA(MoneyCurrency.EUR),
    LUX(MoneyCurrency.EUR),
    LVA(MoneyCurrency.EUR),
    MCO(MoneyCurrency.EUR),
    MLT(MoneyCurrency.EUR),
    NLD(MoneyCurrency.EUR),
    PRT(MoneyCurrency.EUR),
    SVN(MoneyCurrency.EUR),
    SVK(MoneyCurrency.EUR),
    SMR(MoneyCurrency.EUR),
    VAT(MoneyCurrency.EUR),
    USA(MoneyCurrency.USD),
    GBR(MoneyCurrency.GBP),
    SWE(MoneyCurrency.SEK),
    NOR(MoneyCurrency.NOK),
    ISL(MoneyCurrency.ISK),
    CHE(MoneyCurrency.CHF),
    CAN(MoneyCurrency.CAD),
    JPN(MoneyCurrency.JPY),
    NZL(MoneyCurrency.NZD),
    LTU(MoneyCurrency.LTL),
    POL(MoneyCurrency.PLN),
    CZE(MoneyCurrency.CZK),
    HUN(MoneyCurrency.HUF),
    HKG(MoneyCurrency.HKD),
    SGP(MoneyCurrency.SGD),
    ZAF(MoneyCurrency.ZAR),
    BGR(MoneyCurrency.BGN),
    ROU(MoneyCurrency.RON),
    TUR(MoneyCurrency.TRY),
    KOR(MoneyCurrency.KRW),
    HRV(MoneyCurrency.HRK),
    RUS(MoneyCurrency.RUB),
    THA(MoneyCurrency.THB),
    MYS(MoneyCurrency.MYR),
    PHL(MoneyCurrency.PHP),
    IDN(MoneyCurrency.IDR),
    CHN(MoneyCurrency.CHF),
    BRA(MoneyCurrency.BRL),
    MEX(MoneyCurrency.MXN),
    IND(MoneyCurrency.INR),
    ISR(MoneyCurrency.ILS);

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
