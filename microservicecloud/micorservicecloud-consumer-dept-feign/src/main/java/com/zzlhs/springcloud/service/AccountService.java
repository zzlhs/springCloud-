package com.zzlhs.springcloud.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zzlhs.springcloud.dao.AccountMapper;

@Service
public class AccountService {
	
	@Autowired
	private AccountMapper accountMapper;
	
	@Transactional
	public void serviceA() {
		accountMapper.updateMoney(100, 1);
		accountMapper.updateMoney(-100, 2);
		System.out.println("完成...");
	}
	
	@Transactional
	public void serviceB() {
			accountMapper.updateMoney(100, 1);
			int i = 9/0;
			accountMapper.updateMoney(-100, 2);
			System.out.println("完成...");
	}
	
	public void serviceNotTransactional() {
		accountMapper.updateMoney(100, 1);
		int i = 9/0;
		accountMapper.updateMoney(-100, 2);
		System.out.println("完成...");
	}
}
