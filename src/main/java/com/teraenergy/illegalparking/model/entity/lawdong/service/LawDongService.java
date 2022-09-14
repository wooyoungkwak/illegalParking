package com.teraenergy.illegalparking.model.entity.lawdong.service;

import com.teraenergy.illegalparking.model.entity.lawdong.domain.LawDong;

import java.util.List;

/**
 * Date : 2022-09-14
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

public interface LawDongService {

    public LawDong get(Integer lawDongSeq);

    public LawDong getFromLnmadr(String Lnmadr);

    public List<LawDong> gets();

    public void set(LawDong lawDong);

    public void sets(List<LawDong> lawDongs);

    public void delete(LawDong lawDong);

}
