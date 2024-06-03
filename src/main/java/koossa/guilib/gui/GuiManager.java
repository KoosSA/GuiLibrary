package koossa.guilib.gui;

import java.util.HashMap;
import java.util.Map;

import koossa.guilib.elements.GuiElement;
import koossa.guilib.rendering.GuiRenderer;
import koossa.guilib.rendering.RenderBatch;
import koossa.guilib.rendering.RenderUtils;

public class GuiManager {
	
	private GuiRenderer renderer = new GuiRenderer();
	private Map<String, GuiElement> allGuis = new HashMap<String, GuiElement>();
	private Map<String, GuiElement> visibleGuis = new HashMap<String, GuiElement>();
	private Map<String, RenderBatch> renderBatches = new HashMap<String, RenderBatch>();

	public void resize(int width, int height) {
		renderer.resize(width, height);
		allGuis.values().forEach(e -> e.onResize());
		allGuis.forEach((id, ele) -> RenderUtils.updateRenderBatch(ele, renderBatches.get(id)));
	}

	public void update(float delta) {
		visibleGuis.forEach( (id, gui) -> gui.update());
	}

	public void render() {
		visibleGuis.keySet().forEach(id -> {
			renderer.render(renderBatches.get(id));
		});
	}
	
	public void dispose() {
		renderer.dispose();
	}

	public void addGui(String id, GuiElement rootElement) {
		allGuis.putIfAbsent(id, rootElement);
		renderBatches.putIfAbsent(id, new RenderBatch());
		RenderUtils.updateRenderBatch(rootElement, renderBatches.get(id));
	}

	public void show(String id) {
		GuiElement element = allGuis.getOrDefault(id, null);
		if (element == null) return;
		visibleGuis.putIfAbsent(id, element);
	}

	public int hide(String id) {
		visibleGuis.remove(id);
		return visibleGuis.size();
	}

	public void hideAll() {
		visibleGuis.clear();
	}

	public int toggleGui(String id) {
		if (visibleGuis.containsKey(id)) {
			hide(id);
		} else {
			show(id);
		}
		return visibleGuis.size();
	}
	
	

}
