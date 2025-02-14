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

import org.jetbrains.annotations.ApiStatus;
import org.quiltmc.config.api.MetadataContainerBuilder;
import org.quiltmc.config.impl.Comments;

import java.lang.annotation.Annotation;
import java.util.*;

public final class ConfigFieldAnnotationProcessors {
	private static final Map<Class<? extends Annotation>, List<ConfigFieldAnnotationProcessor<?>>> PROCESSORS = new HashMap<>();

	static {
		register(Comment.class, new Comment.Processor());
		register(Comments.class, new Comments.Processor());
		register(IntegerRange.class, new IntegerRange.Processor());
		register(FloatRange.class, new FloatRange.Processor());
		register(Matches.class, new Matches.Processor());
	}

	public static <T extends Annotation> void register(Class<T> annotationClass, ConfigFieldAnnotationProcessor<T> processor) {
		PROCESSORS.computeIfAbsent(annotationClass, c -> new ArrayList<>())
				.add(processor);
	}

	private static <T extends Annotation> void process(ConfigFieldAnnotationProcessor<T> processor, T annotation, MetadataContainerBuilder<?> builder) {
		processor.process(annotation, builder);
	}

	@ApiStatus.Internal
	@SuppressWarnings({"unchecked", "rawtypes"})
	public static void applyAnnotationProcessors(Annotation annotation, MetadataContainerBuilder<?> builder) {
		for (ConfigFieldAnnotationProcessor<?> processor : PROCESSORS.getOrDefault(annotation.annotationType(), Collections.emptyList())) {
			process((ConfigFieldAnnotationProcessor) processor, annotation, builder);
		}
	}
}
