package View;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

public class SceneGestures {


    PannableCanvas canvas;
    private DragContext sceneDragContext = new DragContext();
    private EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {

        public void handle(MouseEvent event) {
            sceneDragContext.mouseAnchorX = event.getSceneX();
            sceneDragContext.mouseAnchorY = event.getSceneY();

            sceneDragContext.translateAnchorX = canvas.getTranslateX();
            sceneDragContext.translateAnchorY = canvas.getTranslateY();

        }

    };
    private EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {
        public void handle(MouseEvent event) {
            canvas.setTranslateX(sceneDragContext.translateAnchorX + event.getSceneX() - sceneDragContext.mouseAnchorX);
            canvas.setTranslateY(sceneDragContext.translateAnchorY + event.getSceneY() - sceneDragContext.mouseAnchorY);

            event.consume();
        }
    };
    /**
     * Mouse wheel handler: zoom to pivot point
     */
    private EventHandler<ScrollEvent> onScrollEventHandler = new EventHandler<ScrollEvent>() {

        @Override
        public void handle(ScrollEvent event) {

            double delta = 2;

            double scale = canvas.getScale();
            double oldScale = scale;
            int resizeval;
            if (event.getDeltaY() < 0 && oldScale != 1.0 * 0.5) {
                resizeval = 1;
                scale /= delta;
                canvas.ResizeGrid(resizeval);
            } else {
                if (event.getDeltaY() >= 0 && oldScale != 8.0) {
                    resizeval = -1;
                    scale *= delta;
                    canvas.ResizeGrid(resizeval);
                }
            }

            double f = (scale / oldScale) - 1;
            double dx = (event.getSceneX() - (canvas.getBoundsInParent().getWidth() / 2 + canvas.getBoundsInParent().getMinX()));
            double dy = (event.getSceneY() - (canvas.getBoundsInParent().getHeight() / 2 + canvas.getBoundsInParent().getMinY()));
            canvas.setScale(scale);
            canvas.setPivot(f * dx, f * dy);
            event.consume();

        }

    };

    public SceneGestures(PannableCanvas canvas) {
        this.canvas = canvas;
    }

    public EventHandler<MouseEvent> getOnMousePressedEventHandler() {
        return onMousePressedEventHandler;
    }

    public EventHandler<MouseEvent> getOnMouseDraggedEventHandler() {
        return onMouseDraggedEventHandler;
    }

    public EventHandler<ScrollEvent> getOnScrollEventHandler() {
        return onScrollEventHandler;
    }


}