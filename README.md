How to use:
1.import PermissionsUtils.java
2.use this code:

                //add permission for the list..and register permission for the manifast.xml. 
                String[] permissions = new String[]{Manifest.permission.CAMERA};
                //and like this.
                PermissionsUtils.getInstance().checkPermissions(MainActivity.this, permissions, permissionsResult, CAMERA_REQUEST_CODE);
                
3.//create interface

        PermissionsUtils.IPermissionsResult permissionsResult = new PermissionsUtils.IPermissionsResult() {
        @Override
        public void passPermissons() {
            //Its request success.
            Toast.makeText(MainActivity.this, "Its request success.", Toast.LENGTH_SHORT).show();
          }
      };
    
4.//create permission request callback.

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    switch (requestCode) {
        case CAMERA_REQUEST_CODE:
            PermissionsUtils.getInstance().onRequestPermissionsResult(this, permissions, grantResults);
            break;
        }
    }
    
5. finish.
