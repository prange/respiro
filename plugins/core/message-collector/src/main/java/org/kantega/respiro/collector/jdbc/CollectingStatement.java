/*
 * Copyright 2015 Kantega AS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kantega.respiro.collector.jdbc;

import org.kantega.respiro.collector.Collector;
import org.kantega.respiro.jdbc.proxy.ProxyStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 */
public class CollectingStatement extends ProxyStatement {
    public CollectingStatement(Statement statement) {
        super(statement);
    }

    @Override
    public boolean execute(String sql) throws SQLException {
        Collector.getCurrent().ifPresent(e -> e.addBackendMessage(new JdbcExchangeMessage(sql)));
        return super.execute(sql);
    }

    @Override
    public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
        Collector.getCurrent().ifPresent(e -> e.addBackendMessage(new JdbcExchangeMessage(sql)));
        return super.execute(sql, autoGeneratedKeys);
    }

    @Override
    public boolean execute(String sql, int[] columnIndexes) throws SQLException {
        Collector.getCurrent().ifPresent(e -> e.addBackendMessage(new JdbcExchangeMessage(sql)));
        return super.execute(sql, columnIndexes);
    }

    @Override
    public boolean execute(String sql, String[] columnNames) throws SQLException {
        Collector.getCurrent().ifPresent(e -> e.addBackendMessage(new JdbcExchangeMessage(sql)));
        return super.execute(sql, columnNames);
    }

    @Override
    public long executeLargeUpdate(String sql) throws SQLException {
        Collector.getCurrent().ifPresent(e -> e.addBackendMessage(new JdbcExchangeMessage(sql)));
        return super.executeLargeUpdate(sql);
    }

    @Override
    public long executeLargeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        Collector.getCurrent().ifPresent(e -> e.addBackendMessage(new JdbcExchangeMessage(sql)));
        return super.executeLargeUpdate(sql, autoGeneratedKeys);
    }

    @Override
    public long executeLargeUpdate(String sql, int[] columnIndexes) throws SQLException {
        Collector.getCurrent().ifPresent(e -> e.addBackendMessage(new JdbcExchangeMessage(sql)));
        return super.executeLargeUpdate(sql, columnIndexes);
    }

    @Override
    public long executeLargeUpdate(String sql, String[] columnNames) throws SQLException {
        Collector.getCurrent().ifPresent(e -> e.addBackendMessage(new JdbcExchangeMessage(sql)));
        return super.executeLargeUpdate(sql, columnNames);
    }

    @Override
    public ResultSet executeQuery(String sql) throws SQLException {
        Collector.getCurrent().ifPresent(e -> e.addBackendMessage(new JdbcExchangeMessage(sql)));
        return super.executeQuery(sql);
    }

    @Override
    public int executeUpdate(String sql) throws SQLException {
        Collector.getCurrent().ifPresent(e -> e.addBackendMessage(new JdbcExchangeMessage(sql)));
        return super.executeUpdate(sql);
    }

    @Override
    public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        Collector.getCurrent().ifPresent(e -> e.addBackendMessage(new JdbcExchangeMessage(sql)));
        return super.executeUpdate(sql, autoGeneratedKeys);
    }

    @Override
    public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
        Collector.getCurrent().ifPresent(e -> e.addBackendMessage(new JdbcExchangeMessage(sql)));
        return super.executeUpdate(sql, columnIndexes);
    }

    @Override
    public int executeUpdate(String sql, String[] columnNames) throws SQLException {
        Collector.getCurrent().ifPresent(e -> e.addBackendMessage(new JdbcExchangeMessage(sql)));
        return super.executeUpdate(sql, columnNames);
    }
}
