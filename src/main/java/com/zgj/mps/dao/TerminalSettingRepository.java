package com.zgj.mps.dao;

import com.zgj.mps.model.TerminalSetting;
import com.zgj.mps.model.TerminalSettingKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TerminalSettingRepository extends JpaRepository<TerminalSetting, TerminalSettingKey> {



}