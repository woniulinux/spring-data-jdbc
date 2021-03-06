/*
 * Copyright 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.jdbc.mapping.model;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.Test;
import org.springframework.data.annotation.Id;
import org.springframework.data.jdbc.mapping.model.JdbcPersistentEntityImplUnitTests.DummySubEntity;

/**
 * Unit tests for the {@link NamingStrategy}.
 *
 * @author Kazuki Shimizu
 * @author Oliver Gierke
 */
public class NamingStrategyUnitTests {

	private final NamingStrategy target = NamingStrategy.INSTANCE;
	private final JdbcMappingContext context = new JdbcMappingContext(target);
	private final JdbcPersistentEntity<?> persistentEntity = context.getRequiredPersistentEntity(DummyEntity.class);

	@Test
	public void getTableName() {

		assertThat(target.getTableName(persistentEntity.getType())).isEqualTo("DummyEntity");
		assertThat(target.getTableName(DummySubEntity.class)).isEqualTo("DummySubEntity");
	}

	@Test
	public void getColumnName() {

		assertThat(target.getColumnName(persistentEntity.getPersistentProperty("id"))).isEqualTo("id");
		assertThat(target.getColumnName(persistentEntity.getPersistentProperty("createdAt"))).isEqualTo("createdAt");
		assertThat(target.getColumnName(persistentEntity.getPersistentProperty("dummySubEntities")))
				.isEqualTo("dummySubEntities");
	}

	@Test
	public void getReverseColumnName() {

		assertThat(target.getReverseColumnName(persistentEntity.getPersistentProperty("dummySubEntities")))
				.isEqualTo("DummyEntity");
	}

	@Test
	public void getKeyColumn() {

		assertThat(target.getKeyColumn(persistentEntity.getPersistentProperty("dummySubEntities")))
				.isEqualTo("DummyEntity_key");
	}

	@Test
	public void getSchema() {
		assertThat(target.getSchema()).isEmpty();
	}

	@Test
	public void getQualifiedTableName() {

		assertThat(target.getQualifiedTableName(persistentEntity.getType())).isEqualTo("DummyEntity");

		NamingStrategy strategy = new NamingStrategy() {
			@Override
			public String getSchema() {
				return "schema";
			}
		};

		assertThat(strategy.getQualifiedTableName(persistentEntity.getType())).isEqualTo("schema.DummyEntity");
	}

	static class DummyEntity {

		@Id int id;
		LocalDateTime createdAt, lastUpdatedAt;
		List<DummySubEntity> dummySubEntities;
	}
}
