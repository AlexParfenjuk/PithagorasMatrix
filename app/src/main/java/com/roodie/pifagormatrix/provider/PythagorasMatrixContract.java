package com.roodie.pifagormatrix.provider;

/**
 * Created by Roodie on 21.03.2015.
 */
public class PythagorasMatrixContract {
  interface UserColumns{

      String BIRTHDAY = "user_birthday";

      String BIRTHMONTH = "user_birthmonth";

      String BIRTHYEAR = "user_birthyear";

      String ID = "user_id";

      String NAME = "user_name";
  }

    interface MatrixColumns {

        String ID = "id";

        String VALUE = "value";

        String TITLE = "title";

        String DESCRIPTION = "description";
    }

}
