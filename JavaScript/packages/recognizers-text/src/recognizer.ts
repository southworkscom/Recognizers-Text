import { IModel, ModelContainer } from "./models"

export abstract class Recognizer<TModelOptions> {
  public readonly RecognizerOptions: TModelOptions;
  public readonly RecognizerCulture: string;

  private readonly modelContainer: ModelContainer = new ModelContainer();

  protected constructor(culture: string, options: TModelOptions) {
    this.RecognizerCulture = culture;
    this.RecognizerOptions = options;
    this.InitializeConfiguration();
  }

  protected abstract InitializeConfiguration();

  getModel(modelTypeName: string, culture: string): IModel {
    return this.modelContainer.getModel(modelTypeName, culture);
  }

  registerModel(modelTypeName: string, culture: string, model: IModel) {
    this.modelContainer.registerModel(modelTypeName, culture, model);
  }
}