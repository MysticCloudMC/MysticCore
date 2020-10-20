package net.mysticcloud.spigot.core.utils.pathfindergoals;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

import net.minecraft.server.v1_16_R2.ControllerLook;
import net.minecraft.server.v1_16_R2.EntityInsentient;
import net.minecraft.server.v1_16_R2.Navigation;
import net.minecraft.server.v1_16_R2.NavigationAbstract;
import net.minecraft.server.v1_16_R2.NavigationFlying;
import net.minecraft.server.v1_16_R2.PathType;
import net.minecraft.server.v1_16_R2.PathfinderGoal;

public class CustomPathfinderGoalFollowEntity extends PathfinderGoal {
	private final EntityInsentient a;

	public EntityInsentient c;

	private final double d;

	private final NavigationAbstract e;

	private int f;

	private final float g;

	private float h;

	private final float i;

	public CustomPathfinderGoalFollowEntity(EntityInsentient var0, double var1, float var3, float var4) {
		this.a = var0;
		this.d = var1;
		this.e = var0.getNavigation();
		this.g = var3;
		this.i = var4;
		a(EnumSet.of(PathfinderGoal.Type.MOVE, PathfinderGoal.Type.LOOK));
		if (!(var0.getNavigation() instanceof Navigation) && !(var0.getNavigation() instanceof NavigationFlying))
			throw new IllegalArgumentException("Unsupported mob type for FollowMobGoal");
	}

	public boolean a() {
		return true;
	}

	public boolean b() {
		return (this.c != null && !this.e.m() && this.a.h(this.c) > (this.g * this.g));
	}

	public void c() {
		this.f = 0;
		this.h = this.a.a(PathType.WATER);
		this.a.a(PathType.WATER, 0.0F);
	}

	public void d() {
		this.c = null;
		this.e.o();
		this.a.a(PathType.WATER, this.h);
	}

	public void e() {
		if (this.c == null || this.a.isLeashed())
			return;
		this.a.getControllerLook().a(this.c, 10.0F, this.a.O());
		if (--this.f > 0)
			return;
		this.f = 10;
		double var0 = this.a.locX() - this.c.locX();
		double var2 = this.a.locY() - this.c.locY();
		double var4 = this.a.locZ() - this.c.locZ();
		double var6 = var0 * var0 + var2 * var2 + var4 * var4;
		if (var6 <= (this.g * this.g)) {
			this.e.o();
			ControllerLook var8 = this.c.getControllerLook();
			if (var6 <= this.g
					|| (var8.d() == this.a.locX() && var8.e() == this.a.locY() && var8.f() == this.a.locZ())) {
				double var9 = this.c.locX() - this.a.locX();
				double var11 = this.c.locZ() - this.a.locZ();
				this.e.a(this.a.locX() - var9, this.a.locY(), this.a.locZ() - var11, this.d);
			}
			return;
		}
		this.e.a(this.c, this.d);
	}
}
