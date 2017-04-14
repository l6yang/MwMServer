package com.mwm.loyal.service;

import com.mwm.loyal.dao.BaseAndroidDao;
import com.mwm.loyal.model.AccountBean;
import com.mwm.loyal.model.FeedBackBean;

import java.util.List;

public class AndroidService implements BaseAndroidService {
    private BaseAndroidDao androidDao;

    @Override
    public AccountBean loginByAccount(AccountBean accountBean) {
        return androidDao.loginByAccount(accountBean);
    }

    @Override
    public AccountBean getUserByAccount(AccountBean accountBean) {
        return androidDao.getUserByAccount(accountBean);
    }

    @Override
    public int registerAccount(AccountBean bean) {
        System.out.println("AndroidService::" + bean.toString());
        return androidDao.registerAccount(bean);
    }

    @Override
    public int updateAccount(AccountBean accountBean) {
        return androidDao.updateAccount(accountBean);
    }

    @Override
    public int destroyAccount(AccountBean accountBean) {
        return androidDao.destroyAccount(accountBean);
    }

    @Override
    public List<FeedBackBean> queryAll() {
        return androidDao.queryAll();
    }

    @Override
    public List<FeedBackBean> queryByAccount(String account) {
        return androidDao.queryByAccount(account);
    }

    @Override
    public int insertFeedBack(FeedBackBean feedBackBean) {
        return androidDao.insertFeedBack(feedBackBean);
    }

    @Override
    public int deleteFeedBack(FeedBackBean feedBackBean) {
        return androidDao.deleteFeedBack(feedBackBean);
    }

    public BaseAndroidDao getAndroidDao() {
        return androidDao;
    }

    public void setAndroidDao(BaseAndroidDao androidDao) {
        this.androidDao = androidDao;
    }
}
