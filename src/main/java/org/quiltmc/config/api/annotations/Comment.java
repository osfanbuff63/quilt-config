/*
 * Copyright 2022 QuiltMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.quiltmc.config.api.annotations;

import org.quiltmc.config.api.Comments;
import org.quiltmc.config.api.Config;
import org.quiltmc.config.api.MetadataContainerBuilder;
import org.quiltmc.config.api.MetadataType;
import org.quiltmc.config.impl.CommentsImpl;

import java.lang.annotation.*;
import java.util.*;

/**
 * Used to annotate fields of classes that represent config files with comments that can be saved to disk or displayed
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Repeatable(org.quiltmc.config.impl.Comments.class)
public @interface Comment {
	/**
	 * A {@link MetadataType} to supply to {@link Config.Builder#metadata}
	 */
	MetadataType<Comments, Builder> TYPE = MetadataType.create(() -> Optional.of(new CommentsImpl(Collections.emptyList())), Builder::new);

	String[] value();

	final class Processor implements ConfigFieldAnnotationProcessor<Comment> {
		@Override
		public void process(Comment comment, MetadataContainerBuilder<?> builder) {
			for (String c : comment.value()) {
				builder.metadata(TYPE, comments -> comments.add(c));
			}
		}
	}

	final class Builder implements MetadataType.Builder<Comments> {
		private final List<String> comments = new ArrayList<>(0);

		public void add(String... comments) {
			this.comments.addAll(Arrays.asList(comments));
		}

		@Override
		public Comments build() {
			return new CommentsImpl(this.comments);
		}
	}
}
