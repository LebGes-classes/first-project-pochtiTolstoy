package gfx;

public class Textures {
  public enum TextureType {
    CAVE_TEXTURE, PATH_TEXTURE,
    PLAYER_TEXTURE, MOUSE_ENEMY, UNDEFINED_TEXTURE
  };

  public static final int[][] caveTexture = {
    {2, 3, 4, 5, 6, 7, 8, 9, 10, 11},
    {'.', '\'', ',', '-', '~', '=', '%', '*','&', '#'}
  };
  public static final int[][] pathTexture = {
    {0, 1},
    {'.', '#'}
  };
  public static final int[][] clearPathTexture = {
    {0},
    {'.'}
  };
  public static final int[][] playerTexture = {
    {100, 101, 102},
    {'@', '\'', '*'}
  };
  public static final int[][] undefinedTexture = {
    {-1},
    {'N'}
  };
  // ~:> <:~ '^' 'v'
  public static final int[][] mouseEnemyTexture = {
    {103, 104, 105, 106, 107,  108},
    {'~', ':', '>', '<', '^', 'v'}
  };
  public static final int[][][] storage = {
    caveTexture, pathTexture, playerTexture, mouseEnemyTexture, undefinedTexture,
  };
  public static final int[][][] pathFinderTextures = {
    clearPathTexture, playerTexture, mouseEnemyTexture
  };
  public static final int storageSize = storage.length;
  public static final int pathTextureSize = pathTexture[0].length;
  public static final int caveTextureSize = caveTexture[0].length;
  public static final int playerTextureSize = playerTexture[0].length;
  public static final int undefinedTextureSize = undefinedTexture[0].length;
  public static final int clearPathTextureSize = clearPathTexture[0].length;
  public static final int pathFinderTexturesSize = pathFinderTextures.length;


  public static char getPixelById(int pixelId) {
    for (int storageId = 0; storageId < storageSize; ++storageId) {
      int[][] texture = storage[storageId];
      for (int i = 0; i < texture[0].length; ++i) {
        if (texture[0][i] == pixelId) {
          return (char) texture[1][i];
        }
      }
    }
    return (char) undefinedTexture[1][0];
  }

  public static TextureType getTypeById(int pixelId) {
    TextureType[] values = TextureType.values();
    for (int storageId = 0; storageId < storageSize; ++storageId) {
      int[][] texture = storage[storageId];
      for (int i = 0; i < texture[0].length; ++i) {
        if (texture[0][i] == pixelId) {
          return values[storageId];
        }
      }
    }
    return TextureType.UNDEFINED_TEXTURE;
  }

  public static boolean isPathFinderPixel(int pixelId) {
    boolean isPathPixel = false;
    TextureType[] values = TextureType.values();
    for (int storageId = 0; storageId < pathFinderTexturesSize; ++storageId) {
      int[][] texture = pathFinderTextures[storageId];
      for (int i = 0; i < texture[0].length; ++i) {
        if (texture[0][i] == pixelId) {
          isPathPixel = true;
        }
      }
    }
    return isPathPixel;
  }
}
