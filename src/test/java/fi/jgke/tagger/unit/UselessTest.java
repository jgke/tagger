package fi.jgke.tagger.unit;

import fi.jgke.tagger.TaggerApplication;
import fi.jgke.tagger.config.DatabaseConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;

import javax.sql.DataSource;


@RunWith(PowerMockRunner.class)
public class UselessTest {

    @PrepareForTest({SpringApplication.class})
    @Test
    public void mainCallsRun() {
        PowerMockito.mockStatic(SpringApplication.class);
        String[] args = new String[3];
        TaggerApplication.main(args);
        PowerMockito.verifyStatic();
        SpringApplication.run(TaggerApplication.class, args);
    }

    @PrepareForTest({DataSourceBuilder.class})
    @Test
    public void databaseConfigCreatesDataSource() {
        DataSource dataSource = Mockito.mock(DataSource.class);
        DataSourceBuilder dataSourceBuilder = Mockito.mock(DataSourceBuilder.class);
        PowerMockito.mockStatic(DataSourceBuilder.class);
        DatabaseConfig dbc = new DatabaseConfig();
        PowerMockito.when(DataSourceBuilder.create()).thenReturn(dataSourceBuilder);
        PowerMockito.when(dataSourceBuilder.build()).thenReturn(dataSource);
        DataSource result = dbc.dataSource();
        Assert.assertEquals(dataSource, result);
    }

}
