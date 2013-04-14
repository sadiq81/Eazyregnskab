package dk.eazyit.eazyregnskab.services;

/**
 * @author Trifork
 */
//public class SortableAccountTransactionDataProvider extends SortableDataProvider<> implements IFilterStateLocator<T> {

//    @SpringBean
//    AccountTransactionDao accountTransactionDao;
//
//    LegalEntity legalEntity;
//
//    public SortableAccountTransactionDataProvider(LegalEntity legalEntity) {
//        // set default sort
//        Injector.get().inject(this);
//        setSort("ownedAccount", SortOrder.ASCENDING);
//        this.legalEntity = legalEntity;
//    }
//
//
//    private AccountTransaction filter = new AccountTransaction();
//
//    @Override
//    public AccountTransaction getFilterState() {
//        return filter;
//    }
//
//    @Override
//    public void setFilterState(AccountTransaction state) {
//        filter = state;
//    }
//
//    public ArrayList<String> getInvestmentItemChoices() {
//
//        List<AccountTransaction> list = accountTransactionDao.findByNamedQuery(AccountTransaction.QUERY_ALL_FOR_ENTITY_OLDEST_FIRST, legalEntity);
//        HashMap<Long, String> map = new HashMap<Long, String>();
//        for (AccountTransaction accountTransaction : list) {
//            if (accountTransaction.getInvestmentItem() != null) {
//                map.put(accountTransaction.getInvestmentItem().getId(), accountTransaction.getInvestmentItem().getName());
//            }
//        }
//        ArrayList<String> names = new ArrayList<String>();
//        for (String name : map.values()) {
//            names.add(name);
//        }
//
//        Collections.sort(names);
//        return names;
//    }
//
//    public ArrayList<String> getOwnedAccountChoices() {
//
//        List<AccountTransaction> list = accountTransactionDao.findByNamedQuery(AccountTransaction.QUERY_ALL_FOR_ENTITY_OLDEST_FIRST, legalEntity);
//        HashMap<Long, String> map = new HashMap<Long, String>();
//        for (AccountTransaction accountTransaction : list) {
//            if (accountTransaction.getInvestmentItem() != null) {
//                map.put(accountTransaction.getOwnedAccount().getId(), accountTransaction.getOwnedAccount().getName());
//            }
//        }
//        ArrayList<String> symbols = new ArrayList<String>();
//        for (String symbol : map.values()) {
//            symbols.add(symbol);
//        }
//
//
//        return symbols;
//    }
//
//    @Override
//    public Iterator<AccountTransaction> iterator(int first, int count) {
//
//        List<AccountTransaction> list = new ArrayList<AccountTransaction>();
//        list.addAll(accountTransactionDao.findByNamedQuery(AccountTransaction.QUERY_ALL_FOR_ENTITY_OLDEST_FIRST, legalEntity));
//
//        list = filterList(list);
//
//        sortList(list, getSort());
//
//        if (first + count > list.size()) {
//            return list.subList(first, list.size()).iterator();
//        } else {
//            return list.subList(first, first + count).iterator();
//        }
//
//    }
//
//    @Override
//    public int size() {
//        return filterList(accountTransactionDao.findByNamedQuery(AccountTransaction.QUERY_ALL_FOR_ENTITY_OLDEST_FIRST, legalEntity)).size();
//    }
//
//    @Override
//    public IModel<AccountTransaction> model(AccountTransaction object) {
//        return new Model<AccountTransaction>(object);
//    }
//
//    private List<AccountTransaction> sortList(List<AccountTransaction> collection, final SortParam sortParam) {
//
//        if (sortParam.getProperty().equals("accountTransactionType")) {
//            Collections.sort(collection, new AccountTransactionEnumComparator(sortParam.isAscending(), "AccountTransactionType"));
//        } else if (sortParam.getProperty().equals("transactionDateTime")) {
//            Collections.sort(collection, new AccountTransactionTransactionDateTimeComparator(sortParam.isAscending()));
//        } else if (sortParam.getProperty().equals("investmentItem")) {
//            Collections.sort(collection, new AccountTransactionInvestmentItemComparator(sortParam.isAscending()));
//        } else if (sortParam.getProperty().equals("ownedAccount")) {
//            Collections.sort(collection, new AccountTransactionOwnedAccountComparator(sortParam.isAscending()));
//        } else if (sortParam.getProperty().equals("moneyCurrency")) {
//            Collections.sort(collection, new AccountTransactionEnumComparator(sortParam.isAscending(), "MoneyCurrency"));
//        } else if (sortParam.getProperty().equals("totalAmount")) {
//            Collections.sort(collection, new AccountTransactionAmountComparator(sortParam.isAscending()));
//        } else if (sortParam.getProperty().equals("accountTransactionStatus")) {
//            Collections.sort(collection, new AccountTransactionEnumComparator(sortParam.isAscending(), "AccountTransactionStatus"));
//        } else if (sortParam.getProperty().equals("accountTransactionErrorType")) {
//            Collections.sort(collection, new AccountTransactionEnumComparator(sortParam.isAscending(), "AccountTransactionErrorType"));
//        } else {
//            throw new IllegalArgumentException(sortParam.getProperty() + " is not a supported sorting property");
//        }
//
//        return collection;
//    }
//
//    private List<AccountTransaction> filterList(List<AccountTransaction> collection) {
//
//        List<AccountTransaction> filtered = new ArrayList<AccountTransaction>();
//
//        filtered.addAll(collection);
//
//        for (AccountTransaction accountTransaction : collection) {
//
//            if (filter.getAccountTransactionType() != null) {
//                if (!(filter.getAccountTransactionType() == accountTransaction.getAccountTransactionType())) {
//                    filtered.remove(accountTransaction);
//                    continue;
//                }
//            }
//
//            if (filter.getInvestmentItem() != null && filter.getInvestmentItem().getName() != null) {
//                if (accountTransaction.getInvestmentItem() == null || !(filter.getInvestmentItem().getName().equals(accountTransaction.getInvestmentItem().getName()))) {
//                    filtered.remove(accountTransaction);
//                    continue;
//                }
//            }
//
//            if (filter.getOwnedAccount() != null && filter.getOwnedAccount().getName() != null) {
//                if (accountTransaction.getOwnedAccount()==null || !(filter.getOwnedAccount().getName().equals(accountTransaction.getOwnedAccount().getName()))) {
//                    filtered.remove(accountTransaction);
//                    continue;
//                }
//            }
//
//            if (filter.getMoneyCurrency() != null) {
//                if (!(filter.getMoneyCurrency() == accountTransaction.getMoneyCurrency())) {
//                    filtered.remove(accountTransaction);
//                    continue;
//                }
//            }
//
//            if (filter.getAccountTransactionStatus() != null) {
//                if (!(filter.getAccountTransactionStatus() == accountTransaction.getAccountTransactionStatus())) {
//                    filtered.remove(accountTransaction);
//                    continue;
//                }
//            }
//
//            if (filter.getAccountTransactionErrorType() != null) {
//                if (!(filter.getAccountTransactionErrorType() == accountTransaction.getAccountTransactionErrorType())) {
//                    filtered.remove(accountTransaction);
//                    continue;
//                }
//            }
//        }
//
//        return filtered;
//    }
//}