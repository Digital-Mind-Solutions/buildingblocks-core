package org.digitalmind.buildingblocks.core.configutils.repository;


import org.digitalmind.buildingblocks.core.configutils.entity.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigurationRepository extends JpaRepository<Configuration, Long> {

    Configuration findByModuleAndSection(String module, String section);

}
