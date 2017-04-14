package com.mwm.loyal.dao;

import com.mwm.loyal.model.AccountBean;
import com.mwm.loyal.model.FeedBackBean;

import java.util.List;

public interface BaseAndroidDao {

    AccountBean loginByAccount(AccountBean accountBean);

    AccountBean getUserByAccount(AccountBean accountBean);

    int registerAccount(AccountBean bean);

    int updateAccount(AccountBean bean);

    int destroyAccount(AccountBean bean);

    List<FeedBackBean> queryAll();

    List<FeedBackBean> queryByAccount(String account);

    int insertFeedBack(FeedBackBean feedBackBean);

    int deleteFeedBack(FeedBackBean feedBackBean);
}
