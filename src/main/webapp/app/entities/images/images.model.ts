import { IImageStoreType } from 'app/entities/image-store-type/image-store-type.model';

export interface IImages {
  id: number;
  imageBlob?: string | null;
  imageBlobContentType?: string | null;
  imageURL?: string | null;
  imageName?: string | null;
  lowResURL?: string | null;
  originalURL?: string | null;
  storeType?: Pick<IImageStoreType, 'id'> | null;
}

export type NewImages = Omit<IImages, 'id'> & { id: null };
