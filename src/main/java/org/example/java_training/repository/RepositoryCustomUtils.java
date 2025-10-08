/*
 * AMELA Technology JSC
 *
 * NOTICE:  All source code, documentation and other information
 * contained herein is, and remains the property of Thor-Amela.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained from Thor-Amela.
 */
package org.example.java_training.repository;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.jpa.QueryHints;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import org.hibernate.type.*;
import org.springframework.orm.jpa.JpaTransactionManager;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class RepositoryCustomUtils {

    /**
     * Execute a SELECT query that returns a result list
     *
     * @param sql                  a native SQL query string
     * @param resultSetMappingName the name of the result set mapping
     * @param parameters           parameters of query
     * @return the result list
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> getResultList(String sql, String resultSetMappingName, Map<String, Object> parameters,
                                     JpaTransactionManager transactionManager) {
        log.debug("SQL: {}", sql);
        log.debug("Parameters: {}", parameters);
        logSQL(sql, parameters);
        EntityManager entityManager = null;
        try {
            entityManager = Objects.requireNonNull(transactionManager.getEntityManagerFactory()).createEntityManager();
            Query query = createQuery(entityManager, sql, resultSetMappingName, parameters);
            return query.getResultList();
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }
    /**
     * Execute a SELECT query that returns a single result
     *
     * @param sql a native SQL query string
     * @return the result
     */
    public <T> T getSingleResult(String sql, JpaTransactionManager transactionManager) {
        return getSingleResult(sql, null, null, transactionManager);
    }

    /**
     * Execute a SELECT query that returns a single result
     *
     * @param sql        a native SQL query string
     * @param parameters parameters of query
     * @return the result
     */
    public <T> T getSingleResult(String sql, Map<String, Object> parameters, JpaTransactionManager transactionManager) {
        return getSingleResult(sql, null, parameters, transactionManager);
    }

    /**
     * Execute a SELECT query that returns a single result
     *
     * @param sql                  a native SQL query string
     * @param resultSetMappingName the name of the result set mapping
     * @return the result
     */
    public <T> T getSingleResult(String sql, String resultSetMappingName, JpaTransactionManager transactionManager) {
        return getSingleResult(sql, resultSetMappingName, null, transactionManager);
    }

    /**
     * Execute a SELECT query that returns a single result
     *
     * @param sql                  a native SQL query string
     * @param resultSetMappingName the name of the result set mapping
     * @param parameters           parameters of query
     * @return the result
     */
    @SuppressWarnings("unchecked")
    public <T> T getSingleResult(String sql, String resultSetMappingName, Map<String, Object> parameters,
                                 JpaTransactionManager transactionManager) {
        log.debug("SQL: {}", sql);
        log.debug("Parameters: {}", parameters);
        logSQL(sql, parameters);
        EntityManager entityManager = null;
        try {
            entityManager = Objects.requireNonNull(transactionManager.getEntityManagerFactory()).createEntityManager();
            Query query = createQuery(entityManager, sql, resultSetMappingName, parameters);
            return (T) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    /**
     * Create an instance of <code>Query</code> for executing
     *
     * @param sql                  a native SQL query string
     * @param resultSetMappingName the name of the result set mapping
     * @param parameters           parameters of query
     * @return the new Query instance
     */
    private Query createQuery(EntityManager entityManager, String sql, String resultSetMappingName,
                              Map<String, Object> parameters) {
        Query query = null;
        if (StringUtils.isEmpty(resultSetMappingName)) {
            query = entityManager.createNativeQuery(sql);
        } else {
            query = entityManager.createNativeQuery(sql, resultSetMappingName);
        }

        if (parameters == null) {
            return query;
        }
        query.setHint(QueryHints.HINT_READONLY, true);

        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        return query;
    }

    /**
     * get return alias columns
     *
     * @param query
     * @return alias columns List
     */
    @SuppressWarnings("rawtypes")
    private List<String> getReturnAliasColumns(NativeQuery query) {
        String sqlQuery = query.getQueryString();
        sqlQuery = sqlQuery.replace("\n", " ");
        sqlQuery = sqlQuery.replace("\t", " ");
        int numOfRightPythis = 0;
        int startPythis = -1;
        int endPythis = 0;
        boolean hasRightPythis = true;
        while (hasRightPythis) {
            char[] arrStr = sqlQuery.toCharArray();
            hasRightPythis = false;
            int idx = 0;
            for (char c : arrStr) {
                if (idx > startPythis) {
                    if ("(".equalsIgnoreCase(String.valueOf(c))) {
                        if (numOfRightPythis == 0) {
                            startPythis = idx;
                        }
                        numOfRightPythis++;
                    } else if (")".equalsIgnoreCase(String.valueOf(c)) && numOfRightPythis > 0) {
                        numOfRightPythis--;
                        if (numOfRightPythis == 0) {
                            endPythis = idx;
                            break;
                        }
                    }
                }
                idx++;
            }
            if (endPythis > 0) {
                sqlQuery = sqlQuery.substring(0, startPythis) + " # " + sqlQuery.substring(endPythis + 1);
                hasRightPythis = true;
                endPythis = 0;
            }
        }

        return aliasColumns(sqlQuery);
    }

    private List<String> aliasColumns(String sqlQuery) {
        List<String> aliasColumns = new ArrayList<>();
        String[] arrStr = sqlQuery.substring(0, sqlQuery.toUpperCase().indexOf(" FROM ")).split(",");
        for (String str : arrStr) {
            String[] temp = str.trim().split(" ");
            String alias = temp[temp.length - 1].trim();
            if (alias.contains(".")) {
                alias = alias.substring(alias.lastIndexOf('.') + 1).trim();
            }
            if (alias.contains(",")) {
                alias = alias.substring(alias.lastIndexOf(',') + 1).trim();
            }
            if (alias.contains("`")) {
                alias = alias.replace("`", "");
            }
            if (!aliasColumns.contains(alias)) {
                aliasColumns.add(alias);
            }
        }
        return aliasColumns;
    }

    private void logSQL(String sql, Map<String,Object> parameters){
        for (String key : parameters.keySet()) {
            Object value = parameters.get(key);
            if(value instanceof Number){
                sql = sql.replaceAll(":"+key,value.toString());
            } else if (value instanceof ArrayList) {
                List<String> array = ((ArrayList<Object>)value).stream().map(Object::toString).collect(Collectors.toList());
                sql = sql.replaceAll(":"+key, String.join(",",array));
            } else{
                sql = sql.replaceAll(":"+key, String.format("'%s'", value));
            }
        }
        System.out.println("SQL => : " + sql);

    }

}
