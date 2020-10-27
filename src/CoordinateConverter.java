/*
 * This class converts Google coordinates into X Y on 2D map
 * 
 * https://stackoverflow.com/questions/14329691/convert-latitude-longitude-point-to-a-pixels-x-y-on-mercator-projection
 */

public final class CoordinateConverter
{
  //  private final int TILE_SIZE = 256;
    private final int TILE_SIZE = 1200;
    private PointXY _pixelOrigin;
    private double _pixelsPerLonDegree;
    private double _pixelsPerLonRadian;

    public CoordinateConverter()
    {
        this._pixelOrigin = new PointXY(TILE_SIZE / 2.0,TILE_SIZE / 2.0);
        this._pixelsPerLonDegree = TILE_SIZE / 360.0;
        this._pixelsPerLonRadian = TILE_SIZE / (2 * Math.PI);
    }

    double bound(double val, double valMin, double valMax)
    {
        double res;
        res = Math.max(val, valMin);
        res = Math.min(res, valMax);
        return res;
    }

    double degreesToRadians(double deg) 
    {
        return deg * (Math.PI / 180);
    }

    double radiansToDegrees(double rad) 
    {
        return rad / (Math.PI / 180);
    }

    PointXY fromLatLngToPoint(double lat, double lng, int zoom)
    {
        PointXY point = new PointXY(0, 0);

        point.x = _pixelOrigin.x + lng * _pixelsPerLonDegree;       

        // Truncating to 0.9999 effectively limits latitude to 89.189. This is
        // about a third of a tile past the edge of the world tile.
        double siny = bound(Math.sin(degreesToRadians(lat)), -0.9999,0.9999);
        point.y = _pixelOrigin.y + 0.5 * Math.log((1 + siny) / (1 - siny)) *- _pixelsPerLonRadian;

        int numTiles = 1 << zoom;
        point.x = point.x * numTiles;
        point.y = point.y * numTiles;
        return point;
     }

    PointXY fromPointToLatLng(PointXY point, int zoom)
    {
        int numTiles = 1 << zoom;
        point.x = point.x / numTiles;
        point.y = point.y / numTiles;       

        double lng = (point.x - _pixelOrigin.x) / _pixelsPerLonDegree;
        double latRadians = (point.y - _pixelOrigin.y) / - _pixelsPerLonRadian;
        double lat = radiansToDegrees(2 * Math.atan(Math.exp(latRadians)) - Math.PI / 2);
        return new PointXY(lat, lng);
    }

    //this is used for testing
    public static void main(String []args) 
    {
        CoordinateConverter gmap2 = new  CoordinateConverter();

      //  PointXY point1 = gmap2.fromLatLngToPoint(41.850033, -87.6500523, 15);
        PointXY point1 = gmap2.fromLatLngToPoint(41.850033, -87.6500523, 2);
        System.out.println(point1.x+"   "+point1.y);
        PointXY point2 = gmap2.fromPointToLatLng(point1,15);
        System.out.println(point2.x+"   "+point2.y);
    }
}
