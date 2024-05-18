package physics.geometry;

import physics.entity.Brick;
import utils.GameConstants;

public class PhysicTools {

    public static boolean checkCollision(Coordinates c, Vector d, double radius, Segment segment, Rotation rotation) {
        double x1 = segment.getStart().getX();
        double y1 = segment.getStart().getY();
        double x2 = segment.getEnd().getX();
        double y2 = segment.getEnd().getY();
        double dx = x2 - x1;
        double dy = y2 - y1;
        double len = Math.sqrt(dx * dx + dy * dy);
        dx /= len;
        dy /= len;
    
        double t = dx * (c.getX() - x1) + dy * (c.getY() - y1);
    
        // Si le point est proche des rebords du segment
        double closestX, closestY;
        if (t < 0) {
            closestX = x1;
            closestY = y1;
        } else if (t > len) {
            closestX = x2;
            closestY = y2;
        } else {
            closestX = x1 + t * dx;
            closestY = y1 + t * dy;
        }
    
        double distance = Math.sqrt((c.getX() - closestX) * (c.getX() - closestX) +
                (c.getY() - closestY) * (c.getY() - closestY));
    
        if (distance <= radius) {
            double normalX = c.getX() - closestX;
            double normalY = c.getY() - closestY;
            double lenNormal = Math.sqrt(normalX * normalX + normalY * normalY);
            normalX /= lenNormal;
            normalY /= lenNormal;
    
            double d1 = d.getX() * normalX + d.getY() * normalY;

            d.setX(d.getX() - 2 * d1 * normalX+(rotation.getEffect())/20*normalY);
            d.setY(d.getY() - 2 * d1 * normalY+(rotation.getEffect())/20*normalX);
            
            return true;
        }
        return false;
    }
    

    

    public static boolean checkCollision(Coordinates c ,Vector d, double radius ,Brick b,Rotation rotation){
        if (intersectBrick(c, (int) radius, b)) {
            for (Segment s : b.getSegments()) {
                if(checkCollision(c,d,radius,s,rotation)){
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean intersectBrick(Coordinates c, int radius, Brick b) {

        double circleDistance_x = Math.abs(c.getX() - b.getC().getX() - GameConstants.BRICK_WIDTH / 2);
        double circleDistance_y = Math.abs(c.getY() - b.getC().getY() - GameConstants.BRICK_HEIGHT / 2);

        if (circleDistance_x > (GameConstants.BRICK_WIDTH / 2 + radius)) {
            return false;
        }
        if (circleDistance_y > (GameConstants.BRICK_HEIGHT / 2 + radius)) {
            return false;
        }

        if (circleDistance_x <= (GameConstants.BRICK_WIDTH / 2)) {
            return true;
        }
        if (circleDistance_y <= (GameConstants.BRICK_HEIGHT / 2)) {
            return true;
        }

        double cornerDistance_sq = (circleDistance_x - GameConstants.BRICK_WIDTH / 2)
                * (circleDistance_x - GameConstants.BRICK_WIDTH / 2)
                + (circleDistance_y - GameConstants.BRICK_HEIGHT / 2)
                        * (circleDistance_y - GameConstants.BRICK_HEIGHT / 2);

        return (cornerDistance_sq <= (radius * radius));
    }

    
    
}
