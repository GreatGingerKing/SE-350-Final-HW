package shop.data;

public interface InventoryMethodFactory {
    Record run(InventorySet I, Video v, int change);
}
