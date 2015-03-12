package robot; 
import java.awt.geom.Point2D;

import renderables.Renderable;

public class HitDetails {
		public HitDetails(Renderable obs, Point2D hit) {
			this.obs = obs;
			this.hit = hit;
		}

		private Renderable obs;
		private Point2D hit;
		

		public Renderable getObs() {
			return obs;
		}

		public Point2D getHit() {
			return hit;
		}

	}