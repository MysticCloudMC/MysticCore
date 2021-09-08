package net.mysticcloud.spigot.core.utils.admin;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.UUID;

import javax.annotation.meta.TypeQualifier;
import javax.annotation.meta.TypeQualifierValidator;
import javax.annotation.meta.When;

import org.bukkit.Bukkit;

@Documented
@TypeQualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface Online {
	When when() default When.ALWAYS;

	static class Checker implements TypeQualifierValidator<Online> {

		public When forConstantValue(Online qualifierqualifierArgument, Object value) {
			if (value instanceof UUID)
				if (Bukkit.getPlayer((UUID) value) == null)
					return When.NEVER;
			return When.ALWAYS;
		}
	}
}
