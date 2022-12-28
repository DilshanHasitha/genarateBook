export interface IImageStoreType {
  id: number;
  imageStoreTypeCode?: string | null;
  imageStoreTypeDescription?: string | null;
  isActive?: boolean | null;
}

export type NewImageStoreType = Omit<IImageStoreType, 'id'> & { id: null };
