package com.badeling.msbot.domain.request.repository;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LastMsgRepository {

    public LastMsgRepository(){
        this.hashList = new ArrayList<>();
    }
    private List<Integer> hashList;


    /**
     * 添加履历过滤相同求情
     * @param hash
     * @return 添加成功/之前已经存在
     */
    public boolean addMsg(int hash){
        if(hashList.contains(hash)){
            return false;
        }

        if(hashList.size() > 127){
            int i = 16;
            while(i-- != 0){
                hashList.remove(i);
            }
        }
        hashList.add(hash);
        return true;
    }
}
