package com.zgj.mps.dao;

import com.zgj.mps.model.Terminal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

public interface TerminalRepository extends JpaRepository<Terminal, Long> {

    @Query(value = "call GetTerminalByMac(?)", nativeQuery = true)
    List<Object[]> terminals(@Param("macs") String macs);


    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update terminal t set t.server_ip = :serverIp, t.server_mac = :serverMac, t.app_version = :appVersion, t.system_version = :nkVersion where t.id = :id", nativeQuery = true)
    int updateTerminalInitInfo(@Param("id") Long id, @Param("serverIp") String serverIp, @Param("serverMac") String serverMac,
                               @Param("appVersion") String appVersion, @Param("nkVersion") String nkVersion);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update terminal t set t.connect_status = 0, t.disconnect_time = :disconnectTime where t.id = :id", nativeQuery = true)
    int updateTerminalDisconnectTime(@Param("id") Long id, @Param("disconnectTime") Timestamp disconnectTime);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update terminal t set t.dev_state = :devState, t.dl_file_size = :dlFileSize, t.useable_space = :useableSpace, t.disk_space = :diskSpace, t.task_name = :taskName, t.play_content = :playContent where t.id = :id", nativeQuery = true)
    int updateTerminalImdStat(@Param("devState") String devState, @Param("dlFileSize") String dlFileSize, @Param("useableSpace") String useableSpace, @Param("diskSpace") String diskSpace, @Param("taskName") String taskName, @Param("playContent") String playContent, @Param("id") Long id);


}