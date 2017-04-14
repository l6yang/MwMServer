package com.mwm.loyal.service;

import com.mwm.loyal.model.AccountBean;
import com.mwm.loyal.model.FeedBackBean;

import java.util.List;

public interface BaseAndroidService {
    AccountBean loginByAccount(AccountBean accountBean);

    AccountBean getUserByAccount(AccountBean accountBean);

    int registerAccount(AccountBean accountBean);

    int updateAccount(AccountBean accountBean);

    int destroyAccount(AccountBean accountBean);

    List<FeedBackBean> queryAll();

    List<FeedBackBean> queryByAccount(String account);

    int insertFeedBack(FeedBackBean feedBackBean);

    int deleteFeedBack(FeedBackBean feedBackBean);
}
