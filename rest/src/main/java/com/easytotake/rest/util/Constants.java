package com.easytotake.rest.util;


public class Constants {

    public class Rest {
        public static final String BASE_URL = "http://192.168.1.35:3001/";
        public static final String MESSENGERS = "messengers";
        public static final String CATEGORIES = "categories";
        public static final String PRODUCTS = "products";
        public static final String PRODUCTS_TOP = "products/top";
        public static final String SHOPPING_CARD = "shoppingCard";
        public static final String UPDATE_SHOPPING_CARD = "updateshoppingcard";

        public static final String GET_SHOPPING_CARD_COUNT = "shoppingCard/count";

    }

    public class Validator {
        public static final String IS_NULL_OR_EMPTY = "^(?=\\s*\\S).*$";
    }
}
