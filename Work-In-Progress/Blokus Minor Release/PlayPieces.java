import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class PlayPieces{

    public static List<Point[]> getPieces(){

        List<Point []> pieces= new ArrayList<>();
        pieces.add(new Point[]{new Point(0,0)});
        pieces.add(new Point[]{new Point(0,0), new Point(1,0)});
        pieces.add(new Point[]{new Point(0,0), new Point(1,0), new Point(2,0)});
        pieces.add(new Point[]{new Point(0,0), new Point(1,0), new Point(0,1)});
        pieces.add(new Point[]{new Point(0,0), new Point(1,0), new Point(2,0), new Point(3,0)});
        pieces.add(new Point[]{new Point(0,0), new Point(1,0), new Point(0,1), new Point(1,1)});
        pieces.add(new Point[]{new Point(0,0), new Point(1,0), new Point(2,0), new Point(0,1)});
        pieces.add(new Point[]{new Point(0,0), new Point(1,0), new Point(2,0), new Point(1,1)});
        pieces.add(new Point[]{new Point(1,0), new Point(2,0), new Point(0,1), new Point(1,1)});
        pieces.add(new Point[]{new Point(0,0), new Point(1,0), new Point(2,0), new Point(3,0), new Point(4,0)});
        pieces.add(new Point[]{new Point(0,0), new Point(1,0), new Point(2,0), new Point(3,0), new Point(0,1)});
        pieces.add(new Point[]{new Point(0,0), new Point(1,0), new Point(2,0), new Point(0,1), new Point(1,1)});
        pieces.add(new Point[]{new Point(0,0), new Point(1,0), new Point(2,0), new Point(1,1), new Point(2,1)});
        pieces.add(new Point[]{new Point(1,0), new Point(2,0), new Point(0,1), new Point(1,1), new Point(2,1)});
        pieces.add(new Point[]{new Point(0,0), new Point(1,0), new Point(2,0), new Point(3,0), new Point(1,1)});
        pieces.add(new Point[]{new Point(1,0), new Point(0,1), new Point(1,1), new Point(2,1), new Point(1,2)});
        pieces.add(new Point[]{new Point(0,0), new Point(0,1), new Point(1,1), new Point(1,2), new Point(2,2)});
        pieces.add(new Point[]{new Point(0,0), new Point(1,0), new Point(2,0), new Point(2,1), new Point(2,2)});
        pieces.add(new Point[]{new Point(0,0), new Point(1,0), new Point(1,1), new Point(2,1), new Point(2,2)});
        pieces.add(new Point[]{new Point(0,0), new Point(0,1), new Point(1,1), new Point(1,2), new Point(2,2)});
        pieces.add(new Point[]{new Point(0,0), new Point(1,0), new Point(2,0), new Point(0,1), new Point(0,2)});
        return pieces;

    }
    public static class GamePiece{

        public Point[] coordinates;
        public int playerNumber;

        public GamePiece(Point[] co, int player){

            this.coordinates = new Point[co.length];
            for (int i=0; i<co.length; i++) this.coordinates[i] = new Point(co[i]);
            this.playerNumber = player;
                
        }
    
        public void rotateClockwise(){
            for (Point p : coordinates){
                int temp = p.x;
                p.x = -p.y;
                p.y = temp;
            }
            normalize();
        }

        private void normalize(){
            int minx = 10, miny = 10;
            for (Point p: coordinates){ minx = Math.min(minx, p.x); miny= Math.min(miny, p.y);}
            for (Point p: coordinates){ p.x -= minx; p.y -= miny;}

        }
        public boolean isAt(int x, int y){

            for (Point p: coordinates) { if (p.x==x && p.y==y) return true;}
            return false;
        }
        public List<Point> getBoardCord (Point base){
            List<Point> list = new ArrayList<>();
            for (Point p: coordinates) list.add(new Point(base.x + p.x, base.y + p.y));
            return list;
        }
        public GamePiece getCopy(){ return new GamePiece(this.coordinates, this.playerNumber);}
    }


}